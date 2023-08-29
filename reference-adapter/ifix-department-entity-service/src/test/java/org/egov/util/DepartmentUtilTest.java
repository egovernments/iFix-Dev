package org.egov.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.common.contract.request.RequestHeader;
import org.egov.config.IfixDepartmentEntityConfig;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentUtilTest {

    @InjectMocks
    private DepartmentUtil departmentUtil;

    @Mock
    private IfixDepartmentEntityConfig ifixDepartmentEntityConfig;

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Spy
    private ObjectMapper objectMapper;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private JsonNode deptSearchResNode;

    @BeforeAll
    void init() throws IOException {
        deptSearchResNode = testDataFormatter.getDepartmentSearchResponse();
    }

    @Test
    void testGetDepartmentFromDepartmentServiceWithValidSearchResult() {
        Map<String, Object> response = objectMapper.convertValue(deptSearchResNode, new TypeReference<Map<String, Object>>() {
        });
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(response);
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentSearchPath())
                .thenReturn("Ifix Master Department Search Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentContextPath())
                .thenReturn("Ifix Master Department Context Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentHost()).thenReturn("localhost");
        List<String> deptList = this.departmentUtil.getDepartmentFromDepartmentService("pb", "8df801cd-d77e-4497-b487-eb1de8825e7b", new RequestHeader());

        assertNotNull(deptList);
        assertTrue(!deptList.isEmpty());

        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentContextPath();
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentHost();
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentSearchPath();
    }

    @Test
    void testGetDepartmentFromDepartmentServiceWithInValidSearchResult() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn(deptSearchResNode);
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentSearchPath())
                .thenReturn("Ifix Master Department Search Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentContextPath())
                .thenReturn("Ifix Master Department Context Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentHost()).thenReturn("localhost");
        assertThrows(CustomException.class,
                () -> this.departmentUtil.getDepartmentFromDepartmentService("pb", "8df801cd-d77e-4497-b487-eb1de8825e7b", new RequestHeader()));

        verify(this.serviceRequestRepository).fetchResult((String) any(), (Object) any());
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentContextPath();
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentHost();
        verify(this.ifixDepartmentEntityConfig).getIfixMasterDepartmentSearchPath();
    }

    @Test
    void testGetDepartmentFromDepartmentServiceWithEmptyTenantId() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentSearchPath())
                .thenReturn("Ifix Master Department Search Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentContextPath())
                .thenReturn("Ifix Master Department Context Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentHost()).thenReturn("localhost");
        assertTrue(this.departmentUtil.getDepartmentFromDepartmentService("", "5fe2fecb-4f36-4259-b769-23263b4e11f0", new RequestHeader()).isEmpty());
    }

    @Test
    void testGetDepartmentFromDepartmentServiceWithEmptyDepartmentId() {
        when(this.serviceRequestRepository.fetchResult((String) any(), (Object) any())).thenReturn("Fetch Result");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentSearchPath())
                .thenReturn("Ifix Master Department Search Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentContextPath())
                .thenReturn("Ifix Master Department Context Path");
        when(this.ifixDepartmentEntityConfig.getIfixMasterDepartmentHost()).thenReturn("localhost");
        assertTrue(this.departmentUtil.getDepartmentFromDepartmentService("pb", "", new RequestHeader()).isEmpty());
    }
}

