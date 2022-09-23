package org.bat2.vacationworker.model.google;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Setter
@Getter
@AllArgsConstructor
public class VacationRecord {
    String name;
    Integer vacNumber;
    String unit;
    Integer object;
    String startDate;
    Integer days;
}
