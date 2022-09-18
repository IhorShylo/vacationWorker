package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
        Gson gson = new Gson();
        try {
            final Request reqModel = gson.fromJson(reader, Request.class);
            logger.info("Action Type:" + reqModel.getAction().getType());
            response.setStatusCode(HttpURLConnection.HTTP_OK);
        } catch (JsonSyntaxException | JsonIOException e) {
            logger.log(Level.WARNING, "Can't parse request body: {}", body);
            logger.log(Level.WARNING, e.getMessage());
            response.setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        }

    }
}
