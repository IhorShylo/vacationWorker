package org.bat2.vacationworker.service.impl;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.bat2.vacationworker.model.google.VacationRecord;
import org.bat2.vacationworker.service.VacationService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class VacationServiceGoogleImpl implements VacationService {

    private static final Logger logger = Logger.getLogger(VacationServiceGoogleImpl.class.getName());
    private static final String SPREADSHEET_ID = "12Y8t1zXRFbtyyRlP_pqmfHRRfrYKJsPXyD3fCGn7Orc";
    private static final String RANGE = "Відпустки!A3:G";
    private final Sheets service;

    public VacationServiceGoogleImpl(Sheets service) {
        this.service = service;
    }

    @Override
    public void saveVacation(VacationRecord vacationRecord) throws IOException {

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
