package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.validator.DepartmentHierarchyLevelValidator;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentHierarchyLevelServiceTest {

    @Mock
    private DepartmentHierarchyLevelEnrichmentService departmentHierarchyLevelEnrichmentService;

    @Mock
    private DepartmentHierarchyLevelRepository departmentHierarchyLevelRepository;

    @InjectMocks
    private DepartmentHierarchyLevelService departmentHierarchyLevelService;

    @Mock
    private DepartmentHierarchyLevelValidator departmentHierarchyLevelValidator;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentHierarchyLevelRequest hierarchyLevelRequest;
    private DepartmentHierarchyLevel hierarchyLevel;
    private DepartmentHierarchyLevelSearchRequest hierarchyLevelSearchRequest;
    private DepartmentHierarchyLevelResponse searchHierarchyLevelResponse;

    @BeforeAll
    void init() throws IOException {
        hierarchyLevelRequest = testDataFormatter.getDeptHierarchyCreateRequestData();
        hierarchyLevel = hierarchyLevelRequest.getDepartmentHierarchyLevel();
        hierarchyLevelSearchRequest = testDataFormatter.getDeptHierarchySearchRequestData();
        searchHierarchyLevelResponse = testDataFormatter.getDeptHierarchySearchResponseData();
    }

    @Test
    void testCreateDepartmentEntityHierarchyLevelWithDefaultRequest() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelCreatePost((DepartmentHierarchyLevelRequest) any());
        doNothing().when(this.departmentHierarchyLevelRepository)
                .save((org.egov.web.models.DepartmentHierarchyLevel) any());
        doNothing().when(this.departmentHierarchyLevelEnrichmentService)
                .enrichHierarchyLevelCreatePost((DepartmentHierarchyLevelRequest) any());
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = new DepartmentHierarchyLevelRequest();
        assertSame(departmentHierarchyLevelRequest,
                this.departmentHierarchyLevelService.createDepartmentEntityHierarchyLevel(departmentHierarchyLevelRequest));
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelCreatePost((DepartmentHierarchyLevelRequest) any());
        verify(this.departmentHierarchyLevelRepository).save((org.egov.web.models.DepartmentHierarchyLevel) any());
        verify(this.departmentHierarchyLevelEnrichmentService)
                .enrichHierarchyLevelCreatePost((DepartmentHierarchyLevelRequest) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithDefaultSearchCriteria() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest.setCriteria(new DepartmentHierarchyLevelSearchCriteria());
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithEmptySearchResult() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest
                .setCriteria(new DepartmentHierarchyLevelSearchCriteria(new ArrayList<>(), "pb", "b32f0cc1-f4b1-4503-95d6-fdf04d0ea2d4",
                        "Zone", 1));
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithSearchResult() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());

        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(searchHierarchyLevelResponse.getDepartmentHierarchyLevel());

        List<DepartmentHierarchyLevel> actualSearchDepartmentEntityHierarchyLevelResult = this.departmentHierarchyLevelService
                .searchDepartmentEntityHierarchyLevel(hierarchyLevelSearchRequest);
        assertSame(searchHierarchyLevelResponse.getDepartmentHierarchyLevel(), actualSearchDepartmentEntityHierarchyLevelResult);
        assertEquals(1, actualSearchDepartmentEntityHierarchyLevelResult.size());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithIdSearchCriteria() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());

        ArrayList<String> idlist = new ArrayList<>();
        idlist.add("6c078c52-4ba0-4678-8e0d-288858d1b72b");
        hierarchyLevelSearchRequest.getCriteria().setIds(idlist);

        assertTrue(this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(hierarchyLevelSearchRequest)
                .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithNullDepartment() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest
                .setCriteria(new DepartmentHierarchyLevelSearchCriteria(new ArrayList<>(), "pb", null, "Zone", 1));
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithEmptyDepartment() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest
                .setCriteria(new DepartmentHierarchyLevelSearchCriteria(new ArrayList<>(), "pb", "", "Zone", 1));
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithDepartmentAndIds() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());
        DepartmentHierarchyLevelSearchCriteria departmentHierarchyLevelSearchCriteria = mock(
                DepartmentHierarchyLevelSearchCriteria.class);
        when(departmentHierarchyLevelSearchCriteria.getDepartmentId()).thenReturn("b32f0cc1-f4b1-4503-95d6-fdf04d0ea2d4");
        when(departmentHierarchyLevelSearchCriteria.getIds()).thenReturn(new ArrayList<>());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest.setCriteria(departmentHierarchyLevelSearchCriteria);
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
        verify(departmentHierarchyLevelSearchCriteria).getDepartmentId();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getIds();
    }

    @Test
    void testSearchDepartmentEntityHierarchyLevelWithLabelAndIds() {
        doNothing().when(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any())).thenReturn(new ArrayList<>());
        DepartmentHierarchyLevelSearchCriteria departmentHierarchyLevelSearchCriteria = mock(
                DepartmentHierarchyLevelSearchCriteria.class);
        when(departmentHierarchyLevelSearchCriteria.getLabel()).thenReturn("Department");
        when(departmentHierarchyLevelSearchCriteria.getDepartmentId()).thenReturn(null);
        when(departmentHierarchyLevelSearchCriteria.getIds()).thenReturn(new ArrayList<>());

        DepartmentHierarchyLevelSearchRequest departmentHierarchyLevelSearchRequest = new DepartmentHierarchyLevelSearchRequest();
        departmentHierarchyLevelSearchRequest.setCriteria(departmentHierarchyLevelSearchCriteria);
        assertTrue(
                this.departmentHierarchyLevelService.searchDepartmentEntityHierarchyLevel(departmentHierarchyLevelSearchRequest)
                        .isEmpty());
        verify(this.departmentHierarchyLevelValidator)
                .validateHierarchyLevelSearchPost((DepartmentHierarchyLevelSearchRequest) any());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((DepartmentHierarchyLevelSearchCriteria) any());
        verify(departmentHierarchyLevelSearchCriteria).getDepartmentId();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getIds();
        verify(departmentHierarchyLevelSearchCriteria).getLabel();
    }
}

