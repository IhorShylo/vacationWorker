package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.HttpURLConnection;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VacationWorkerFunction implements HttpFunction {
    private static final Logger logger = Logger.getLogger(VacationWorkerFunction.class.getName());
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {


        BufferedWriter writer = response.getWriter();
        final BufferedReader reader = request.getReader();
        final String contentType = request.getContentType().orElse("");
        writer.write("Content type is: " + contentType + System.lineSeparator());
        final String requestBody = reader.lines().collect(Collectors.joining());
        writer.write("request body: " + System.lineSeparator() + requestBody);

        printLogs(requestBody);
        response.setStatusCode(HttpURLConnection.HTTP_OK);
    }

    private static void printLogs(String body) {
        System.out.println("I am a log to stdout! Request body:" + body);
        System.err.println("I am a log to stderr! Request body:" + body);

        logger.info("I am an info log! Request body:" + body);
        logger.warning("I am a warning log! Request body:" + body);
    }


}
