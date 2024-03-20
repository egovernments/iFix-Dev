package org.digit.exchange.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExchangeType {

    PROGRAM("program"),
    ON_PROGRAM("on-program"),
    SANCTION("sanction"),
    ON_SANCTION("on-sanction"),
    ALLOCATION("allocation"),
    ON_ALLOCATION("on-allocation"),
    DISBURSE("disburse"),
    ON_DISBURSE("on-disburse"),
    DEMAND("demand"),
    ON_DEMAND("on-demand"),
    RECEIPT("receipt"),
    ON_RECEIPT("on-receipt"),
    BILL("bill"),
    ON_BILL("on-bill");

    private String value;

    ExchangeType(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ExchangeType fromValue(String text) {
        for (ExchangeType b : ExchangeType.values()) {
            if (String.valueOf(b.value).equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
