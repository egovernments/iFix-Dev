package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.MasterDataServiceConfiguration;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class MasterDepartmentUtilTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceConfiguration configuration;

    @Mock
    private ServiceRequestRepository searchRequestRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MasterDepartmentUtil masterDepartmentUtil;

    private ArrayList<String> idList;
    private RequestHeader requestHeader = new RequestHeader();
    private List<Department> departmentList;
    private Object departmentSearchResponseObject;
    private DepartmentResponse departmentResponse;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");

        departmentSearchResponseObject = testDataFormatter.getDepartmentSearchResponseDataAsObject();

        departmentResponse = testDataFormatter.getDepartmentSearchResponseData();

        departmentList = departmentResponse.getDepartment();

        doReturn(new String()).when(configuration).getIfixMasterDepartmentHost();
        doReturn(new String()).when(configuration).getIfixMasterDepartmentContextPath();
        doReturn(new String()).when(configuration).getIfixMasterDepartmentSearchPath();

    }


    @Test
    void testFetchDepartment() {
        doReturn(departmentSearchResponseObject).when(searchRequestRepository).fetchResult((String) any(), any());

        doReturn(departmentList).when(objectMapper).convertValue((Object) any(), (TypeReference) any());

        List<Department> actualDepartmentList = masterDepartmentUtil.fetchDepartment(idList, "pb", requestHeader);

        assertSame(actualDepartmentList, departmentList);
    }

    @Test
    void testFalseFetchDepartment() {
        List<Department> actualDepartmentList = masterDepartmentUtil
                .fetchDepartment(null, null, null);

        assertSame(Collections.emptyList(), actualDepartmentList);
    }


    @Test
    void testFetchDepartmentException() {
        doReturn(new Object()).when(searchRequestRepository).fetchResult((String) any(), any());

        assertThrows(CustomException.class,
                () -> masterDepartmentUtil.fetchDepartment(idList, "pb", requestHeader));
    }

}

