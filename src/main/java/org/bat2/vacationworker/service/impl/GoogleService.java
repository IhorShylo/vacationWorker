package org.bat2.vacationworker.service.impl;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.service.VacationService;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleService implements VacationService {

    private static final Logger logger = Logger.getLogger(GoogleService.class.getName());
    private static final String APPLICATION_NAME = "Vacation worker";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);
    public static final String SPREADSHEET_ID = "12Y8t1zXRFbtyyRlP_pqmfHRRfrYKJsPXyD3fCGn7Orc";
    public static final String RANGE = "Відпустки!A3:G";
    private final byte[] secret;

    public GoogleService(byte[] secret) {
        this.secret = secret;
    }

    public void saveVacation(VacationRecord vacationRecord) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredentials googleCredentials;


        if (secret == null) {
            logger.severe("Secret data is corrupted. Can't save vacation record");
            return;
        }

        InputStream in = new ByteArrayInputStream(secret);
        googleCredentials = GoogleCredentials
                .fromStream(in)
                .createScoped(SCOPES);
        googleCredentials.refreshIfExpired();
//        AccessToken token = credentials.getAccessToken();

        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(APPLICATION_NAME)
                .build();

        try {
            // Append values to the specified range.
            List<List<Object>> values = List.of(
                    List.of(vacationRecord.getName(), "Мета", vacationRecord.getVacNumber(),
                            vacationRecord.getUnit(), vacationRecord.getObject(),
                            vacationRecord.getStartDate(), vacationRecord.getDays())
            );
            ValueRange body = new ValueRange()
                    .setValues(values);
            AppendValuesResponse result = service.spreadsheets().values().append(SPREADSHEET_ID, RANGE, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            // Prints the spreadsheet with appended values.
            logger.log(Level.INFO, "%d cells appended.", result.getUpdates().getUpdatedCells());
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            logger.severe(error.getMessage());
            if (error.getCode() == 404) {
                logger.warning("Spreadsheet not found with id'" + SPREADSHEET_ID + "'.\n");
            } else {
                logger.severe(error.getMessage());
            }
        }
    }
}
