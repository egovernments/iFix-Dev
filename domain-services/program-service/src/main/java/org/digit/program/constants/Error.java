package org.digit.program.constants;

public class Error {

    public static final String PROGRAM_CODE_ERROR_ALLOCATION = "PROGRAM_CODE_ERROR";
    public static final String PROGRAM_CODE_ERROR_ALLOCATION_MSG = "Program code should be same for all allocations";

    public static final String LOCATION_CODE_ERROR = "LOCATION_CODE_ERROR";
    public static final String LOCATION_CODE_ERROR_MSG = "Location code should be same for all allocations";

    public static final String SANCTIONS_NOT_FOUND = "SANCTIONS_NOT_FOUND";
    public static final String SANCTIONS_NOT_FOUND_MSG = "No sanction found for id(s): ";

    public static final String PROGRAM_CODE_ERROR_SANCTION = "PROGRAM_CODE_ERROR_SANCTION";
    public static  final String PROGRAM_CODE_ERROR_SANCTION_MSG = "Program code should be same as sanction program code";

    public static final String SANCTIONED_AMOUNT_ERROR_ALLOCATION = "SANCTIONED_AMOUNT_ERROR_ALLOCATION";
    public static final String SANCTIONED_AMOUNT_ERROR_ALLOCATION_MSG = "Sanctioned amount should be greater than allocated amount";

    public static final String AVAILABLE_AMOUNT_ERROR_DEDUCTION = "AVAILABLE_AMOUNT_ERROR_DEDUCTION";
    public static final String AVAILABLE_AMOUNT_ERROR_DEDUCTION_MSG = "Available amount should be greater than deduction amount";

    public static final String INVALID_ALLOCATION_ID = "INVALID_ALLOCATION_ID";
    public static final String INVALID_ALLOCATION_ID_MSG = "Allocation id cannot be empty";

    public static final String ALLOCATIONS_NOT_FOUND = "ALLOCATIONS_NOT_FOUND";
    public static final String ALLOCATIONS_NOT_FOUND_MSG =  "No allocation found for id(s): ";

    public static final String DUPLICATE_ALLOCATION_ID = "DUPLICATE_ALLOCATION_ID";
    public static final String DUPLICATE_ALLOCATION_ID_MSG = "Duplicate allocation id(s): ";

    public static final String NO_PROGRAMS_FOUND = "NO_PROGRAMS_FOUND";
    public static final String NO_PROGRAMS_FOUND_MSG = "No program found for code: ";

    public static final String RECEIVER_ID_SENDER_ID_ERROR = "RECEIVER_ID_SENDER_ID_ERROR";
    public static final String RECEIVER_ID_SENDER_ID_ERROR_MSG = "ReceiverId should not be same as SenderId";

    public static final String RECEIVER_ID_ERROR = "RECEIVER_ID_ERROR";
    public static final String RECEIVER_ID_ERROR_MSG = "ReceiverId is wrong, it should have @";

    public static final String SENDER_ID_ERROR = "SENDER_ID_ERROR";
    public static final String SENDER_ID_ERROR_MSG = "SenderId is wrong, it should have @";

    public static final String ACTION_ERROR = "ACTION_ERROR";
    public static final String ACTION_ERROR_MSG = "Action in request header should be same as url";

    public static final String MESSAGE_TYPE_ERROR = "MESSAGE_TYPE_ERROR";
    public static final String MESSAGE_TYPE_ERROR_MSG = "MessageType in request header should be same as url";

    public static final String RECEIVER_ID_CLIENT_HOST_URL_ERROR = "RECEIVER_ID_CLIENT_HOST_URL_ERROR";
    public static final String RECEIVER_ID_CLIENT_HOST_URL_ERROR_MSG = "ReceiverId should be same as client host url";

    public static final String CHILD_DISBURSEMENT_ERROR = "CHILD_DISBURSEMENT_ERROR";
    public static final String CHILD_DISBURSEMENT_ERROR_MSG = "Child disbursements should not be null or empty";

    public static final String INDIVIDUAL_ERROR = "INDIVIDUAL_ERROR";
    public static final String INDIVIDUAL_ERROR_MSG = "Individual should not be null";

    public static final String ACCOUNT_CODE_ERROR = "ACCOUNT_CODE_ERROR";
    public static final String ACCOUNT_CODE_ERROR_MSG = "Account code should not be null or empty";

    public static final String MULTIPLE_CHILD_DISBURSEMENT_ERROR = "MULTIPLE_CHILD_DISBURSEMENT_ERROR";
    public static final String MULTIPLE_CHILD_DISBURSEMENT_ERROR_MSG = "Currently child disbursement should not have child disbursements";

    public static final String DISBURSEMENT_LOCATION_CODE_ERROR = "DISBURSEMENT_LOCATION_CODE_ERROR";
    public static final String DISBURSEMENT_LOCATION_CODE_ERROR_MSG = "Disbursement location code should be same as child disbursement location code";

    public static final String DISBURSEMENT_PROGRAM_CODE_ERROR = "DISBURSEMENT_PROGRAM_CODE_ERROR";
    public static final String DISBURSEMENT_PROGRAM_CODE_ERROR_MSG = "Disbursement program code should be same as child disbursement program code";

    public static final String DISBURSEMENT_NET_AMOUNT_ERROR = "DISBURSEMENT_NET_AMOUNT_ERROR";
    public static final String DISBURSEMENT_NET_AMOUNT_ERROR_MSG = "Disbursement amount should be equal to child disbursement net amount";

    public static final String DISBURSEMENT_GROSS_AMOUNT_ERROR = "DISBURSEMENT_GROSS_AMOUNT_ERROR";
    public static final String DISBURSEMENT_GROSS_AMOUNT_ERROR_MSG = "Disbursement amount should be equal to child disbursement gross amount";

    public static final String DISBURSEMENT_SANCTION_ID_ERROR = "DISBURSEMENT_SANCTION_ID_ERROR";
    public static final String DISBURSEMENT_SANCTION_ID_ERROR_MSG = "Disbursement sanction id should be same as child disbursement sanction id";

    public static final String NO_SANCTION_FOUND = "NO_SANCTION_FOUND";
    public static final String NO_SANCTION_FOUND_MSG = "No sanction found for id: ";

    public static final String SANCTION_AVAILABLE_AMOUNT_ERROR = "SANCTION_AVAILABLE_AMOUNT_ERROR";
    public static final String SANCTION_AVAILABLE_AMOUNT_ERROR_MSG = "Sanction available amount should be greater than disbursement amount";

    public static final String DISBURSEMENT_ID_NULL_ERROR = "DISBURSEMENT_ID_NULL_ERROR";
    public static final String DISBURSEMENT_ID_NULL_ERROR_MSG = "Disbursement id should not be null or empty";

    public static final String DISBURSEMENT_ID_ERROR = "DISBURSEMENT_ID_ERROR";
    public static final String DISBURSEMENT_ID_ERROR_MSG = "Disbursement id should be unique";

    public static final String NO_DISBURSEMENT_FOUND = "NO_DISBURSEMENT_FOUND";
    public static final String NO_DISBURSEMENT_FOUND_MSG = "No disbursement found for id: ";

    public static final String CHILD_DISBURSEMENT_ID_ERROR = "CHILD_DISBURSEMENT_ID_ERROR";
    public static final String CHILD_DISBURSEMENT_ID_ERROR_MSG = "Child disbursement id should be present in db";

    public static final String DISBURSEMENT_ALREADY_PRESENT_ERROR = "DISBURSEMENT_ALREADY_PRESENT_ERROR";
    public static final String DISBURSEMENT_ALREADY_PRESENT_ERROR_MSG = "Disbursement already present for target id: ";

    public static final String TRANSACTION_ID_MANDATORY = "TRANSACTION_ID_MANDATORY";
    public static final String TRANSACTION_ID_MANDATORY_MSG = "Transaction id is mandatory";

    public static final String START_DATE_ERROR = "START_DATE_ERROR";
    public static final String START_DATE_ERROR_MSG = "StartDate should not be empty";

    public static final String DATES_ERROR = "DATES_ERROR";
    public static final String DATES_ERROR_MSG = "StartDate should be less than endDate";

    public static final String PROGRAM_PARENT_ID_NOT_FOUND = "PROGRAM_PARENT_ID_NOT_FOUND";
    public static final String PROGRAM_PARENT_ID_NOT_FOUND_MSG = "Program not found for ParentId: ";

    public static final String PROGRAM_ID_ERROR = "PROGRAM_ID_ERROR";
    public static final String PROGRAM_ID_ERROR_MSG = "programId should not be empty for update";

    public static final String PROGRAM_NOT_FOUND = "PROGRAM_NOT_FOUND";
    public static final String PROGRAM_NOT_FOUND_MSG = "Program not found for id: ";

    public static final String PROGRAM_CODE_ERROR = "PROGRAM_CODE_ERROR";
    public static final String PROGRAM_CODE_ERROR_MSG = "programCode should not be empty";

    public static final String PROGRAM_FOUND_FOR_CREATE = "PROGRAM_FOUND_FOR_CREATE";
    public static final String PROGRAM_FOUND_FOR_CREATE_MSG = "Program already present for id: ";

    public static final String SAME_PROGRAM_CODE = "SAME_PROGRAM_CODE";
    public static final String SAME_PROGRAM_CODE_MSG = "Program code should be same for all Sanction";

    public static final String SAME_LOCATION_CODE = "SAME_LOCATION_CODE";
    public static final String SAME_LOCATION_CODE_MSG = "Location code should be same for all Sanction";

    public static final String DUPLICATE_SANCTION_ID = "DUPLICATE_SANCTION_ID";
    public static final String DUPLICATE_SANCTION_ID_MSG = "Duplicate sanction id(s): ";

    public static final String SANCTION_ID_NULL = "SANCTION_ID_NULL";
    public static final String SANCTION_ID_NULL_MSG = "Sanction id cannot be null or empty";

    public static final String NO_SANCTIONS_FOUND = "NO_SANCTIONS_FOUND";
    public static final String NO_SANCTIONS_FOUND_MSG = "No sanction found for id(s): ";

    public static final String NO_SANCTION_AVAILABLE_FOR_AMOUNT = "NO_SANCTION_AVAILABLE_FOR_AMOUNT";
    public static final String NO_SANCTION_AVAILABLE_FOR_AMOUNT_MSG = "No sanction available for disburse amount ";

    public static final String IDGEN_ERROR = "IDGEN_ERROR";
    public static final String IDGEN_ERROR_MSG = "No ids returned from IdGen Service";

    public static final String PARSING_ERROR = "PARSING_ERROR";
    public static final String PARSING_ERROR_MSG = "Error while parsing ";
}
