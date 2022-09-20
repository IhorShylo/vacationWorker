package org.bat2.vacationworker.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.bat2.vacationworker.ecxeptions.InvalidCardNameException;
import org.bat2.vacationworker.ecxeptions.UnexpectedRequestException;
import org.bat2.vacationworker.ecxeptions.UnsupportedTrelloActionException;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.model.trello.*;
import org.bat2.vacationworker.service.VacationService;
import org.bat2.vacationworker.service.impl.GoogleService;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VacationWorkerFunction implements HttpFunction {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    public static final String TARGET_LIST_ID = "62f227668555a62731adef73";
    public static final String VALID_ACTION_TYPE = "updateCard";
    public static final String VALID_TRANSLATION_KEY = "action_move_card_from_list_to_list";

    private final VacationService vacationService = new GoogleService();

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        try {
            logger.info("Start request processing");
            final String cardName = processRequest(request);
            logger.info("Request processed successfully");

            logger.info("Start card name processing");
            final VacationRecord vacationRecord = parseName(cardName);
            logger.info("Card name processing finished successfully");

            logger.info("Start vacation record saving");
            vacationService.saveVacation(vacationRecord);
            logger.info("Vacation record saved successfully");

            response.setStatusCode(HttpURLConnection.HTTP_OK);
        } catch (JsonProcessingException e) {
            logger.warning("Can't parse request JSON. " + e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (IOException e) {
            logger.warning("Invalid request reader. " + e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (UnexpectedRequestException e) {
            logger.warning(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        } catch (UnsupportedTrelloActionException e) {
            logger.info(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_UNSUPPORTED_TYPE);
        } catch (InvalidCardNameException e) {
            logger.severe(e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        }


    }

    private String processRequest(HttpRequest request) throws IOException {
        final BufferedReader reader = request.getReader();
        final String body = reader.lines().collect(Collectors.joining());
        final String method = request.getMethod();

        if (!"POST".equals(method) || body.isEmpty()) {
            throw new UnexpectedRequestException("Invalid request. Request method: " + method + "; Request body: " + body);
        }

        ObjectMapper mapper = new ObjectMapper();

        final TrelloRequest reqModel = mapper.readValue(body, TrelloRequest.class);
        final Action action = reqModel.getAction();
        String type = null;
        String translationKey = null;
        String listAfterId = null;
        String cardName = null;
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

                final Card card = data.getCard();
                if (card != null) {
                    cardName = card.getName();
                }
            }

        }

        if (!VALID_ACTION_TYPE.equals(type) || !VALID_TRANSLATION_KEY.equals(translationKey) || !TARGET_LIST_ID.equals(listAfterId)) {
            throw new UnsupportedTrelloActionException("Unsupported trello action. Processing ignored. Action type: " + type +
                    ", translation key: " + translationKey + ", listAfterId: " + listAfterId);
        }

        return cardName;
    }


    private static VacationRecord parseName(String cardName) {
        if (cardName == null || cardName.isBlank()) {
            throw new InvalidCardNameException("Invalid card name: " + cardName);
        }

        String name = null;
        Integer vacNumber = null;
        String unit = null;
        Integer object = null;
        String startDate = null;
        Integer days = null;


        //TODO implement parsing logic

        return new VacationRecord(name, vacNumber, unit, object, startDate, days);
    }
}
