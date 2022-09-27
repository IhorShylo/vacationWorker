package org.bat2.vacationworker.service;

import org.bat2.vacationworker.model.google.VacationRecord;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface VacationService {

    void saveVacation(VacationRecord vacationRecord) throws GeneralSecurityException, IOException;
}
