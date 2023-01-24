package org.egov.util;

import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class DepartmentUtilTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceConfiguration configuration;

    @Mock
    private ServiceRequestRepository searchRequestRepository;

    @InjectMocks
    private DepartmentUtil departmentUtil;

    private Object departmentEntityResponse;
    private ArrayList<String> idList;
    private RequestHeader requestHeader = new RequestHeader();

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");

        departmentEntityResponse = testDataFormatter.getDepartmentEntitySearchResponseDataAsObject();

        doReturn(new String()).when(configuration).getDepartmentEntityHost();
        doReturn(new String()).when(configuration).getDepartmentEntityContextPath();
        doReturn(new String()).when(configuration).getDepartmentEntitySearchPath();
    }

    @Test
    void testValidateDepartmentEntity() {
        doReturn(departmentEntityResponse).when(searchRequestRepository).fetchResult((String) any(), any());

        assertTrue(departmentUtil.validateDepartmentEntityIds("pb", idList, requestHeader));
    }

    @Test
    void testFalseValidateDepartmentEntity() {

        assertFalse(departmentUtil.validateDepartmentEntityIds(null, new ArrayList(), requestHeader));
    }

    @Test
    void testValidateDepartmentEntityException() {
        doReturn(new Object()).when(searchRequestRepository).fetchResult((String) any(), any());

        assertThrows(CustomException.class,
                () -> departmentUtil.validateDepartmentEntityIds("pb", idList, requestHeader));
    }*/
}

