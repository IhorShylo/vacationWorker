package org.bat2.vacationworker.functions;

import org.apache.commons.lang3.StringUtils;
import org.bat2.vacationworker.client.impl.TrelloClientImpl;
import org.bat2.vacationworker.ecxeptions.UnexpectedRequestException;
import org.bat2.vacationworker.ecxeptions.UnsupportedTrelloActionException;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.*;
import org.bat2.vacationworker.service.CardService;
import org.bat2.vacationworker.service.SecretService;
import org.bat2.vacationworker.service.VacationService;
import org.bat2.vacationworker.service.impl.CardServiceImpl;
import org.bat2.vacationworker.service.impl.GoogleService;

import java.net.http.HttpClient;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class VacationWorkerFunction implements Consumer<TrelloRequest> {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    public static final String TARGET_LIST_ID = "62f227668555a62731adef73";
    public static final String VALID_ACTION_TYPE = "updateCard";
    public static final String VALID_TRANSLATION_KEY = "action_move_card_from_list_to_list";

    private final SecretService secretService = SecretService.getInstance();
    private final VacationService vacationService = new GoogleService(secretService.getApiKeyBytes());
    private final CardService cardService = new CardServiceImpl(
            new TrelloClientImpl(HttpClient.newBuilder().build(), secretService.getTrelloToken()));


    @Override
    public void accept(TrelloRequest request) {
        logger.info("Start function execution");
        try {
            logger.info("Start request body processing");
            final Card card = processRequestBody(request);
            logger.info("Request body processed successfully");

            logger.info("Start card name processing");
            VacationRecord vacationRecord = cardService.parseName(card.getName());
            if (StringUtils.isBlank(vacationRecord.getName()) || StringUtils.isBlank(vacationRecord.getStartDate())
                    || vacationRecord.getDays() == null) {
                logger.warning("Card name processing finished unsuccessfully. Parsed object: " + vacationRecord);
                return;
            }
            logger.info("Card name processing finished successfully. Parsed object: " + vacationRecord);

            logger.info("Start card label processing");
            final String labelName = cardService.getLabelName(card.getId());
            logger.info("Card label processing finished successfully. Label name is: '" + labelName + "'");

            vacationRecord.setUnit(labelName);

            logger.info("Start vacation record saving");
            vacationService.saveVacation(vacationRecord);
            logger.info("Vacation record saved successfully");
        } catch (UnexpectedRequestException e) {
            logger.warning(e.getMessage());
        } catch (UnsupportedTrelloActionException e) {
            logger.info(e.getMessage());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private Card processRequestBody(TrelloRequest request) {
        final Action action = request.getAction();
        String type = null;
        String translationKey = null;
        String listAfterId = null;
        Card card = null;
        if (action != null) {

            type = action.getType();
            final Display display = action.getDisplay();
            if (display != null) {
                translationKey = display.getTranslationKey();
            }
            final Data data = action.getData();
            if (data != null) {
                final BoardList listAfter = data.getListAfter();

                if (listAfter != null) {
                    listAfterId = listAfter.getId();
                }

                card = data.getCard();
                if (card == null || card.getName() == null ||
                        card.getName().isBlank() ||
                        card.getId() == null ||
                        card.getId().isBlank()) {
                    throw new UnexpectedRequestException("Invalid request. Card data is corrupted");
                }
            }

        }

        if (!VALID_ACTION_TYPE.equals(type) || !VALID_TRANSLATION_KEY.equals(translationKey) || !TARGET_LIST_ID.equals(listAfterId)) {
            throw new UnsupportedTrelloActionException("Unsupported trello action. Processing ignored. Action type: " + type +
                    ", translation key: " + translationKey + ", listAfterId: " + listAfterId);
        }

        return card;
    }

}
