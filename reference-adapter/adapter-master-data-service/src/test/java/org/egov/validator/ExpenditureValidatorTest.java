package org.egov.validator;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.TestDataFormatter;
import org.egov.tracer.model.CustomException;
import org.egov.util.MasterDepartmentUtil;
import org.egov.web.models.Department;
import org.egov.web.models.Expenditure;
import org.egov.web.models.ExpenditureRequest;
import org.egov.web.models.ExpenditureSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExpenditureValidatorTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private ExpenditureValidator expenditureValidator;

    @Mock
    private MasterDepartmentUtil masterDepartmentUtil;

    private ExpenditureSearchRequest expenditureSearchRequest;
    private ExpenditureRequest expenditureCreateRequest;

    @BeforeEach
    public void setup() throws IOException {
        expenditureSearchRequest = testDataFormatter.getExpenditureSearchRequestData();
        expenditureCreateRequest = testDataFormatter.getExpenditureCreateRequestData();
    }

    @Test
    void testValidateExpenditureSearchRequestTenantIdException() {
        expenditureSearchRequest.getCriteria().setTenantId("");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest),
                "Tenant id is missing in request data");
    }

    @Test
    void testValidateExpenditureSearchRequestTenantIdLengthException() {
        expenditureSearchRequest.getCriteria().setTenantId("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest),
                "Tenant id length is invalid");
    }

    @Test
    void testValidateExpenditureSearchRequestNameException() {
        expenditureSearchRequest.getCriteria().setName("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest),
                "Expenditure name length is invalid.");
    }

    @Test
    void testValidateExpenditureSearchRequestCodeException() {
        expenditureSearchRequest.getCriteria().setCode("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureSearchRequest(expenditureSearchRequest),
                "Expenditure code length is invalid.");
    }


    @Test
    void testValidateExpenditureCreateRequestHeaderException() {
        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(new ExpenditureRequest()),
                "Request header is missing");
    }

    @Test
    void testValidateExpenditureCreateRequestUserInfoException() {
        expenditureCreateRequest.getRequestHeader().setUserInfo(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "User info is missing");
    }

    @Test
    void testValidateExpenditureCreateRequestExpenditureException() {
        expenditureCreateRequest.setExpenditure(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure request is invalid");
    }

    @Test
    void testValidateExpenditureCreateRequestTenantIdException() {
        expenditureCreateRequest.getExpenditure().setTenantId(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Tenant id is mandatory");
    }

    @Test
    void testValidateExpenditureCreateRequestTenantIdLengthException() {
        expenditureCreateRequest.getExpenditure().setTenantId("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Tenant id length is invalid.");
    }

    @Test
    void testValidateExpenditureCreateRequestCodeException() {
        expenditureCreateRequest.getExpenditure().setCode(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure code is missing in request data");
    }

    @Test
    void testValidateExpenditureCreateRequestCodeLengthException() {
        expenditureCreateRequest.getExpenditure().setCode("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure code length is invalid.");
    }

    @Test
    void testValidateExpenditureCreateRequestNameException() {
        expenditureCreateRequest.getExpenditure().setName(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure name is missing in request data");
    }

    @Test
    void testValidateExpenditureCreateRequestNameLengthException() {
        expenditureCreateRequest.getExpenditure().setName("0");

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure name length is invalid.");
    }

    @Test
    void testValidateExpenditureCreateRequestTypeException() {
        expenditureCreateRequest.getExpenditure().setType(null);

        assertThrows(CustomException.class,
                () -> this.expenditureValidator.validateExpenditureCreateRequest(expenditureCreateRequest),
                "Expenditure type is missing in request data.");
    }


    @Test
    void testValidateExpenditureCreateRequest13() {
        ArrayList<Department> departmentList = new ArrayList<Department>();
        departmentList.add(new Department());
        when(this.masterDepartmentUtil.fetchDepartment((List<String>) any(), (String) any(), (RequestHeader) any()))
                .thenReturn(departmentList);
        Expenditure expenditure = mock(Expenditure.class);
        when(expenditure.getDepartmentId()).thenReturn("42");
        when(expenditure.getType()).thenReturn(Expenditure.TypeEnum.SCHEME);
        when(expenditure.getName()).thenReturn("Name");
        when(expenditure.getCode()).thenReturn("Code");
        when(expenditure.getTenantId()).thenReturn("42");
        ExpenditureRequest expenditureRequest = mock(ExpenditureRequest.class);
        when(expenditureRequest.getExpenditure()).thenReturn(expenditure);
        ArrayList<String> roles = new ArrayList<String>();
        when(expenditureRequest.getRequestHeader()).thenReturn(new RequestHeader(1L, "1.0.2", "42",
                new UserInfo("01234567-89AB-CDEF-FEDC-BA9876543210", roles, new ArrayList<String>(), "Attributes"), "42",
                "Signature"));
        this.expenditureValidator.validateExpenditureCreateRequest(expenditureRequest);
        verify(this.masterDepartmentUtil).fetchDepartment((List<String>) any(), (String) any(), (RequestHeader) any());
        verify(expenditureRequest).getExpenditure();
        verify(expenditureRequest).getRequestHeader();
        verify(expenditure, atLeast(1)).getCode();
        verify(expenditure, atLeast(1)).getDepartmentId();
        verify(expenditure, atLeast(1)).getName();
        verify(expenditure, atLeast(1)).getTenantId();
        verify(expenditure).getType();
    }
}

