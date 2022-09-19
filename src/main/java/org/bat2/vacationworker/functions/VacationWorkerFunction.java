package org.bat2.vacationworker.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.bat2.vacationworker.model.Request;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VacationWorkerFunction implements HttpFunction {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    public static final String TARGET_LIST_ID = "62f227668555a62731adef73";
    public static final String VALID_ACTION_TYPE = "updateCard";
    public static final String VALID_TRANSLATION_KEY = "action_move_card_from_list_to_list";


    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        final BufferedReader reader = request.getReader();
        final String body = reader.lines().collect(Collectors.joining());
        if (body.isEmpty()) {
            logger.log(Level.WARNING, "Invalid request: req body is empty");
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }
        logger.info("Request body: " + body);
        ObjectMapper mapper = new ObjectMapper();

        try {
            final Request reqModel = mapper.readValue(body, Request.class);
            final String type = reqModel.getAction().getType();
            final String translationKey = reqModel.getAction().getDisplay().getTranslationKey();
            final String cardName = reqModel.getAction().getData().getCard().getName();
            logger.log(Level.INFO, "Card name:" + cardName);
            logger.log(Level.INFO, "Action Type:" + type);
            logger.log(Level.INFO, "Translation key: " + translationKey);
            response.setStatusCode(HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Something went wrong. Request body: {}", body);
            logger.log(Level.WARNING, e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        }

    }
}
