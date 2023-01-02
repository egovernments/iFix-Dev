package org.egov.validator;

import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.repository.GovernmentRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Government;
import org.egov.web.models.GovernmentRequest;
import org.egov.web.models.GovernmentSearchCriteria;
import org.egov.web.models.GovernmentSearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class GovernmentValidatorTest {
    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private GovernmentRepository governmentRepository;

    @InjectMocks
    private GovernmentValidator governmentValidator;

    private GovernmentRequest governmentRequest;
    private RequestHeader requestHeader = new RequestHeader();
    private ArrayList<String> idList;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");
    }

    @BeforeEach
    public void setup() throws IOException {
        governmentRequest = testDataFormatter.getGovernmentRequestData();
    }

    @Test
    void testValidateGovernmentRequestDataPayloadException() {
        assertThrows(CustomException.class, () -> governmentValidator.validateGovernmentRequestData(null),
                "Request payload is missing some value");
    }

    @Test
    void testValidateGovernmentRequestDataUserInfoException() {
        RequestHeader requestHeader = new RequestHeader();
        Government government = new Government();
        GovernmentRequest governmentRequest = new GovernmentRequest(requestHeader, government);
        assertThrows(CustomException.class, () -> governmentValidator
                        .validateGovernmentRequestData(governmentRequest),
                "User information is missing");
    }

    @Test
    void testValidateGovernmentRequestDataTenantIdException() {
        governmentRequest.getGovernment().setId(null);
        assertThrows(CustomException.class,
                () -> governmentValidator.validateGovernmentRequestData(governmentRequest),
                "Government Id (Tenant id) is missing in request data");
    }

    @Test
    void testValidateGovernmentRequestDataTenantNameException() {
        governmentRequest.getGovernment().setName(null);
        assertThrows(CustomException.class,
                () -> governmentValidator.validateGovernmentRequestData(governmentRequest),
                "Government name is missing in request data");
    }

    @Test
    void testValidateGovernmentRequestDataDuplicateException() {
        assertNotNull(governmentRequest.getGovernment().getId());

        doReturn(governmentRequest.getGovernment()).when(governmentRepository)
                .findById(governmentRequest.getGovernment().getId());

        assertThrows(CustomException.class,
                () -> governmentValidator.validateGovernmentRequestData(governmentRequest),
                "Duplicate government id");
    }

    @Test
    void testValidateGovernmentRequestData() {
        assertNotNull(governmentRequest.getGovernment().getId());

        doReturn(new Government()).when(governmentRepository)
                .findById("0");

        governmentValidator.validateGovernmentRequestData(governmentRequest);

        verify(governmentRepository).findById(governmentRequest.getGovernment().getId());
    }


    @Test
    void testValidateGovernmentSearchRequestDataPayloadException() {
        GovernmentSearchRequest governmentSearchRequest = new GovernmentSearchRequest();
        assertThrows(CustomException.class,
                () -> governmentValidator.validateGovernmentSearchRequestData(governmentSearchRequest));
        assertThrows(CustomException.class, () -> governmentValidator.validateGovernmentSearchRequestData(null),
                "Request payload is missing some value");
    }

    @Test
    void testValidateGovernmentSearchRequestData2() {
        RequestHeader requestHeader = new RequestHeader();
        GovernmentSearchCriteria searchCriteria = new GovernmentSearchCriteria();
        GovernmentSearchRequest governmentSearchRequest = new GovernmentSearchRequest(requestHeader, searchCriteria);
        assertThrows(CustomException.class, () -> governmentValidator.validateGovernmentSearchRequestData(governmentSearchRequest));
    }

    @Test
    void testValidateGovernmentSearchRequestDataWithNullCriteria() {
        RequestHeader requestHeader = new RequestHeader();
        GovernmentSearchRequest governmentSearchRequest = new GovernmentSearchRequest(requestHeader, null);
        assertThrows(CustomException.class, () -> governmentValidator.validateGovernmentSearchRequestData(
                governmentSearchRequest));
    }
}

