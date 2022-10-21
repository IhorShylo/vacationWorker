package org.bat2.vacationworker;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class VacationWorkerApp {

    @Value("${sm://api-key}")
    private byte[] secret;

    public static void main(String[] args) {
        SpringApplication.run(VacationWorkerApp.class, args);
    }

    @Bean
    public HttpClient getHttpClient() {
        return HttpClient.newBuilder().build();
    }

    @Bean
    public JsonFactory getJsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    public Sheets getSheets() throws GeneralSecurityException, IOException {
        final List<String> SCOPES =
                Collections.singletonList(SheetsScopes.SPREADSHEETS);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        final String APPLICATION_NAME = "Vacation worker";

        GoogleCredentials googleCredentials;

        InputStream in = new ByteArrayInputStream(secret);
        googleCredentials = GoogleCredentials
                .fromStream(in)
                .createScoped(SCOPES);
        googleCredentials.refreshIfExpired();
//        AccessToken token = credentials.getAccessToken();

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


}
