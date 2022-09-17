package org.bat2.vacationworker.functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;

public class VacationWorkerFunction implements HttpFunction {
    public static final String API_KEY = "d2dfccf7084e0a6b400742de00baa8d0";
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        writer.write("My API KEY: " + API_KEY);
    }
}
