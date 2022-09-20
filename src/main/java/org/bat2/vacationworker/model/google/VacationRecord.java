package org.bat2.vacationworker.model.google;

import lombok.Value;

@Value
public class VacationRecord {
    String name;
    Integer vacNumber;
    String unit;
    Integer object;
    String startDate;
    Integer days;
}
