package org.egov.util;

import org.egov.config.TestDataFormatter;
import org.egov.repository.DepartmentHierarchyLevelRepository;
import org.egov.web.models.DepartmentHierarchyLevel;
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
class DepartmentHierarchyUtilTest {

    @Mock
    private DepartmentHierarchyLevelRepository departmentHierarchyLevelRepository;

    @InjectMocks
    private DepartmentHierarchyUtil departmentHierarchyUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentHierarchyLevel hierarchyLevel;

    @BeforeAll
    void init() throws IOException {
        hierarchyLevel = testDataFormatter.getDeptHierarchyCreateRequestData().getDepartmentHierarchyLevel();
    }

    @Test
    void testValidateHierarchyLevelMetaDataWithEmptySearchResult() {
        ArrayList<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        List<DepartmentHierarchyLevel> actualValidateHierarchyLevelMetaDataResult = this.departmentHierarchyUtil
                .validateHierarchyLevelMetaData("b32f0cc1-f4b1-4503-95d6-fdf04d0ea2d4", 1, "pb");
        assertSame(departmentHierarchyLevelList, actualValidateHierarchyLevelMetaDataResult);
        assertTrue(actualValidateHierarchyLevelMetaDataResult.isEmpty());
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any());
    }

    @Test
    void testValidateHierarchyLevelMetaDataWithNullDepartmentId() {
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(new ArrayList<>());
        assertTrue(this.departmentHierarchyUtil.validateHierarchyLevelMetaData(null, 1, "pb").isEmpty());
    }

    @Test
    void testValidateHierarchyLevelMetaDataWithEmptyDepartmentId() {
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(new ArrayList<>());
        assertTrue(this.departmentHierarchyUtil.validateHierarchyLevelMetaData("", 1, "pb").isEmpty());
    }

    @Test
    void testValidateHierarchyLevelMetaDataWithNullHierarchyLevel() {
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(new ArrayList<>());
        assertTrue(this.departmentHierarchyUtil.validateHierarchyLevelMetaData("42", null, "42").isEmpty());
    }

    @Test
    void testValidateHierarchyLevelMetaDataWithHierarchyLevelSearchResult() {
        List<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        departmentHierarchyLevelList.add(hierarchyLevel);
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        assertFalse(this.departmentHierarchyUtil.validateHierarchyLevelMetaData("b32f0cc1-f4b1-4503-95d6-fdf04d0ea2d4", 1, "pb").isEmpty());
    }
}

