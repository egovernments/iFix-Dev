package org.egov.validator;

import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.TestDataFormatter;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentUtil;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentHierarchyLevelValidatorTest {

    @Mock
    private DepartmentHierarchyLevelRepository departmentHierarchyLevelRepository;

    @InjectMocks
    private DepartmentHierarchyLevelValidator departmentHierarchyLevelValidator;

    @Mock
    private DepartmentUtil departmentUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentHierarchyLevelRequest hierarchyLevelRequest;
    private DepartmentHierarchyLevel hierarchyLevel;
    private DepartmentHierarchyLevelSearchRequest hierarchyLevelSearchRequest;
    private DepartmentHierarchyLevelResponse searchHierarchyLevelResponse;

    @BeforeEach
    void init() throws IOException {
        hierarchyLevelRequest = testDataFormatter.getDeptHierarchyCreateRequestData();
        hierarchyLevel = hierarchyLevelRequest.getDepartmentHierarchyLevel();
        hierarchyLevelSearchRequest = testDataFormatter.getDeptHierarchySearchRequestData();
        searchHierarchyLevelResponse = testDataFormatter.getDeptHierarchySearchResponseData();
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithDefaultRequest() {
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator
                .validateHierarchyLevelCreatePost(new DepartmentHierarchyLevelRequest()));
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithDefaultRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(
                new DepartmentHierarchyLevelRequest(requestHeader, new DepartmentHierarchyLevel())));
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithDefaultHierarchyLevelInRequest() {
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = mock(DepartmentHierarchyLevelRequest.class);
        when(departmentHierarchyLevelRequest.getRequestHeader()).thenReturn(new RequestHeader());
        when(departmentHierarchyLevelRequest.getDepartmentHierarchyLevel()).thenReturn(new DepartmentHierarchyLevel());
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(departmentHierarchyLevelRequest));
        verify(departmentHierarchyLevelRequest).getDepartmentHierarchyLevel();
        verify(departmentHierarchyLevelRequest).getRequestHeader();
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithRequestHeaderAndDefaultHierarchyLevel() {
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = mock(DepartmentHierarchyLevelRequest.class);
        when(departmentHierarchyLevelRequest.getRequestHeader())
                .thenReturn(hierarchyLevelRequest.getRequestHeader());
        when(departmentHierarchyLevelRequest.getDepartmentHierarchyLevel()).thenReturn(new DepartmentHierarchyLevel());
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(departmentHierarchyLevelRequest));
        verify(departmentHierarchyLevelRequest).getDepartmentHierarchyLevel();
        verify(departmentHierarchyLevelRequest).getRequestHeader();
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithNullUUID() {
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = mock(DepartmentHierarchyLevelRequest.class);
        when(departmentHierarchyLevelRequest.getRequestHeader()).thenReturn(requestHeader);
        when(departmentHierarchyLevelRequest.getDepartmentHierarchyLevel()).thenReturn(null);
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(departmentHierarchyLevelRequest));
        verify(departmentHierarchyLevelRequest).getDepartmentHierarchyLevel();
        verify(departmentHierarchyLevelRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithEmptyGovernmentAndDepartmentSearchResult() {
        when(this.departmentUtil.getDepartmentFromDepartmentService((String) any(), (String) any(), (RequestHeader) any()))
                .thenReturn(new ArrayList<>());

        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(hierarchyLevelRequest));
        verify(this.departmentUtil).getDepartmentFromDepartmentService((String) any(), (String) any(),
                (RequestHeader) any());
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithEmptyDepartmentSearchResult() {
        when(this.departmentUtil.getDepartmentFromDepartmentService((String) any(), (String) any(), (RequestHeader) any()))
                .thenReturn(new ArrayList<>());
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(hierarchyLevelRequest));
        verify(this.departmentUtil).getDepartmentFromDepartmentService((String) any(), (String) any(),
                (RequestHeader) any());
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithNullTenant() {
        when(this.departmentUtil.getDepartmentFromDepartmentService((String) any(), (String) any(), (RequestHeader) any()))
                .thenReturn(new ArrayList<>());
        hierarchyLevelRequest.getDepartmentHierarchyLevel().setTenantId(null);
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(hierarchyLevelRequest));
    }

    @Test
    void testValidateHierarchyLevelCreatePostWithEmptyTenantId() {
        when(this.departmentUtil.getDepartmentFromDepartmentService((String) any(), (String) any(), (RequestHeader) any()))
                .thenReturn(new ArrayList<>());
        hierarchyLevelRequest.getDepartmentHierarchyLevel().setTenantId("");
        assertThrows(CustomException.class,
                () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelCreatePost(hierarchyLevelRequest));
    }

    @Test
    void testValidateHierarchyLevelSearchPostWithDefaultSearchRequest() {
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator
                .validateHierarchyLevelSearchPost(new DepartmentHierarchyLevelSearchRequest()));
    }

    @Test
    void testValidateHierarchyLevelSearchPostWithDefaultSearchCriteria() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator.validateHierarchyLevelSearchPost(
                new DepartmentHierarchyLevelSearchRequest(requestHeader, new DepartmentHierarchyLevelSearchCriteria())));
    }

    @Test
    void testValidateHierarchyLevelSearchPostWithNullCriteria() {
        hierarchyLevelSearchRequest.setCriteria(null);
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator
                .validateHierarchyLevelSearchPost(hierarchyLevelSearchRequest));
    }

    @Test
    void testValidateHierarchyLevelSearchPostWithTenantIdSearchResult() {
        this.departmentHierarchyLevelValidator.validateHierarchyLevelSearchPost(hierarchyLevelSearchRequest);
    }


    @Test
    void testValidateHierarchyLevelSearchPostWithEmptyTenantId() {
        hierarchyLevelSearchRequest.getCriteria().setTenantId("");
        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelValidator
                .validateHierarchyLevelSearchPost(hierarchyLevelSearchRequest));
    }
}

