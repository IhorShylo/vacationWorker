package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

public class VacationWorkerFunction implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        final BufferedReader reader = request.getReader();
        final String contentType = request.getContentType().orElse("");
        writer.write("Content type is: " + contentType + System.lineSeparator());
        final String requestBody = reader.lines().collect(Collectors.joining());
        writer.write("request body: " + System.lineSeparator() + requestBody);

        response.setStatusCode(HttpURLConnection.HTTP_OK);
    }
}
