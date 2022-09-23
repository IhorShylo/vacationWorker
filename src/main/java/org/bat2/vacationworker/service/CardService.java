package org.bat2.vacationworker.service;

import org.bat2.vacationworker.model.google.VacationRecord;

public interface CardService {

    String getLabelName(String cardId);

    VacationRecord parseName(String cardName);
}
