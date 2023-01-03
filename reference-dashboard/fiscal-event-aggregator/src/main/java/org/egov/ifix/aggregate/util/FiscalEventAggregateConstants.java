package org.egov.ifix.aggregate.util;

public class FiscalEventAggregateConstants {

    private FiscalEventAggregateConstants() {
    }

    public static final String CURRENT_FISCAL_YEAR = "CURRENT_FISCAL_YEAR";
    public static final String PREVIOUS_FISCAL_YEAR = "PREVIOUS_FISCAL_YEAR";
    public static final int DEFAULT_HIERARCHY_LEVEL = 10;
    public static final String VER = "2.0.0";

    public static final String EVENT_TYPE_DEMAND = "Demand";
    public static final String EVENT_TYPE_RECEIPT = "Receipt";
    public static final String EVENT_TYPE_PAYMENT = "Payment";
    public static final String EVENT_TYPE_BILL = "Bill";

    public static final String EVENT_TYPE_PENDING_COLLECTION = "Pending_Collection";
    public static final String EVENT_TYPE_PENDING_PAYMENT = "Pending_Payment";

    public static final String GP_ID = "attributes.departmentEntity.ancestry[6].id";
}
