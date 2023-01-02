package org.egov.util;

public class PspclIfixAdapterConstant {

    public static final String PATH_FETCH_PSPCL_BILL = "fetchPspclBill.xml";
    public static final String PATH_FETCH_PSPCL_PAYMENT = "fetchPspclPayment.xml";

    public static final String PLACEHOLDER_ACCOUNT_NO = "${accountNo}";

    public static final String TAG_GET_BILLS_RESULT = "GetBillsResult";
    public static final String TAG_GET_PAYMENTS_RESULT = "GetPaymentsResult";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String BILL_ISSUE_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String TXN_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    public static final String EVENT_TYPE_DEMAND = "DEMAND";
    public static final String EVENT_TYPE_RECEIPT = "RECEIPT";

    public static final String PSPCL_JOB_NAME = "job-fetch-and-reconcile-pspcl-bill-payment";
    public static final String PSPCL_JOB_GROUP_NAME = "job-group-pspcl-event";

    public static final String LOG_INFO_PREFIX = "<<< INFO >>> ";
    public static final String LOG_ERROR_PREFIX = "<<< ERROR >>> ";
    public static final String RECOVERABLE_ERROR = "### RECOVERABLE ERROR ###";
    public static final String NON_RECOVERABLE_ERROR = "### NON RECOVERABLE ERROR ###";
    public static final String NA = "N/A";
    public static final String EMPTY_FISCAL_EVENT = "EMPTY FISCAL EVENT";

    public static final String MDMS_MODULE_NAME = "pspcl-integration";
    public static final String MDMS_ACCOUNT_GP_MAPPING_MASTER_NAME = "accountNumberGpMapping";

    public static final String FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY = "departmentEntity";
    public static final String FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_CODE = "code";
    public static final String FE_ADDITIONAL_ATTRIBUTE_DEPARTMENT_ENTITY_NAME = "name";
    public static final String FE_ADDITIONAL_ATTRIBUTE_ACCOUNT_NUMBER = "pspclAccountNumber";
}
