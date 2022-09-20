package org.bat2.vacationworker.service;

import org.bat2.vacationworker.model.google.VacationRecord;

public interface VacationService {

    void saveVacation(VacationRecord vacationRecord);
}
