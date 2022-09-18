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

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        final BufferedReader reader = request.getReader();
        final String body = reader.lines().collect(Collectors.joining());
        ObjectMapper mapper = new ObjectMapper();

        if (!body.isEmpty() && "POST".equals(request.getMethod())) {
            try {
                final Request reqModel = mapper.readValue(body, Request.class);
                final String type = reqModel.getAction().getType();
                final String translationKey = reqModel.getAction().getDisplay().getTranslationKey();
                logger.log(Level.INFO, "Action Type:" + type);
                logger.log(Level.INFO, "Translation key: " + translationKey);
                response.setStatusCode(HttpURLConnection.HTTP_OK);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Can't parse request body: {}", body);
                logger.log(Level.WARNING, e.getMessage());
                response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
            }
        }

    }
}
