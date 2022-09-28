package org.bat2.vacationworker.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.bat2.vacationworker.client.impl.TrelloClientImpl;
import org.bat2.vacationworker.ecxeptions.InvalidCardNameException;
import org.bat2.vacationworker.ecxeptions.UnexpectedRequestException;
import org.bat2.vacationworker.ecxeptions.UnsupportedTrelloActionException;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.*;
import org.bat2.vacationworker.service.CardService;
import org.bat2.vacationworker.service.SecretService;
import org.bat2.vacationworker.service.VacationService;
import org.bat2.vacationworker.service.impl.CardServiceImpl;
import org.bat2.vacationworker.service.impl.GoogleService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class VacationWorkerFunction implements HttpFunction {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    public static final String TARGET_LIST_ID = "62f227668555a62731adef73";
    public static final String VALID_ACTION_TYPE = "updateCard";
    public static final String VALID_TRANSLATION_KEY = "action_move_card_from_list_to_list";

    private final SecretService secretService = SecretService.getInstance();
    private final VacationService vacationService = new GoogleService(secretService.getApiKeyBytes());
    private final CardService cardService = new CardServiceImpl(
            new TrelloClientImpl(HttpClient.newBuilder().build(), secretService.getTrelloToken()));

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        BufferedWriter writer = response.getWriter();
        try {
            logger.info("Start request processing");
            final Card card = processRequest(request);
            logger.info("Request processed successfully");

            logger.info("Start card name processing");
            VacationRecord vacationRecord = cardService.parseName(card.getName());
            if (StringUtils.isBlank(vacationRecord.getName()) || StringUtils.isBlank(vacationRecord.getStartDate())
                    || vacationRecord.getDays() == null) {
                logger.warning("Card name processing finished unsuccessfully. Parsed object: " + vacationRecord);
                writer.write("Card name processing finished unsuccessfully. Parsed object: " + vacationRecord);
                response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
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
            writer.write("Card: " + card.getName() + " saved successfully");
            response.setStatusCode(HttpURLConnection.HTTP_OK);
        } catch (UnexpectedRequestException e) {
            writer.write(e.getMessage());
            logger.warning(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (UnsupportedTrelloActionException e) {
            writer.write(e.getMessage());
            logger.info(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        } catch (InvalidCardNameException e) {
            writer.write(e.getMessage());
            logger.severe(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (Exception e) {
            writer.write(e.getMessage());
            logger.severe(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
        }


    }

    private Card processRequest(HttpRequest request) throws IOException {
        logger.info("Encoding: " + request.getCharacterEncoding());
        final BufferedReader reader = request.getReader();
//        final String body = reader.lines().collect(Collectors.joining());
        final String body = StringUtils.toEncodedString(readerToBytes(reader), StandardCharsets.UTF_8);
        final String method = request.getMethod();
        logger.info("Processed body: " + body);

        if (!"POST".equals(method) || body.isEmpty()) {
            throw new UnexpectedRequestException("Invalid request. Request method: " + method + "; Request body: " + body);
        }

        ObjectMapper mapper = new ObjectMapper();

        final TrelloRequest reqModel = mapper.readValue(body, TrelloRequest.class);
        final Action action = reqModel.getAction();
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

    private byte[] readerToBytes(Reader initialReader)
            throws IOException {

        char[] charArray = new char[8 * 1024];
        StringBuilder builder = new StringBuilder();
        int numCharsRead;
        while ((numCharsRead = initialReader.read(charArray, 0, charArray.length)) != -1) {
            builder.append(charArray, 0, numCharsRead);
        }

        initialReader.close();
        return builder.toString().getBytes();
    }
}
