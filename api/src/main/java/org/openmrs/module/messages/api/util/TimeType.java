package org.openmrs.module.messages.api.util;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum TimeType {
    DAY(ChronoUnit.DAYS), MONTH(ChronoUnit.MONTHS), YEAR(ChronoUnit.YEARS);

    private final TemporalUnit temporalUnit;

    TimeType(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }
}
