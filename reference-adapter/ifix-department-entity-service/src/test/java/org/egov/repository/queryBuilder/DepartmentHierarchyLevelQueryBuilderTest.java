package org.egov.repository.queryBuilder;

//import org.bson.Document;
import org.egov.web.models.DepartmentHierarchyLevelSearchCriteria;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.query.Meta;
//import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentHierarchyLevelQueryBuilderTest {

    @InjectMocks
    private DepartmentHierarchyLevelQueryBuilder departmentHierarchyLevelQueryBuilder;

   /* @Test
    void testBuildSearchQueryWithDefaultSearchCriteria() {
        Query actualBuildSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildSearchQuery(new DepartmentHierarchyLevelSearchCriteria());
        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(1, actualBuildSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildSearchQueryWithEmptyIdsList() {
        Query actualBuildSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildSearchQuery(new DepartmentHierarchyLevelSearchCriteria(new ArrayList<>(), "pb", "7bdf9514-e2e5-4563-bfea-f5aaa41b2137",
                        "Zone", 1));
        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(4, actualBuildSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildSearchQueryWithId() {
        DepartmentHierarchyLevelSearchCriteria departmentHierarchyLevelSearchCriteria = new DepartmentHierarchyLevelSearchCriteria();
        departmentHierarchyLevelSearchCriteria.addIdsItem("tenantId");
        Query actualBuildSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildSearchQuery(departmentHierarchyLevelSearchCriteria);
        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(2, actualBuildSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildSearchQueryWithSearchCriteria() {
        DepartmentHierarchyLevelSearchCriteria departmentHierarchyLevelSearchCriteria = mock(
                DepartmentHierarchyLevelSearchCriteria.class);
        when(departmentHierarchyLevelSearchCriteria.getIds()).thenReturn(new ArrayList<>());
        when(departmentHierarchyLevelSearchCriteria.getLevel()).thenReturn(1);
        when(departmentHierarchyLevelSearchCriteria.getLabel()).thenReturn("Zone");
        when(departmentHierarchyLevelSearchCriteria.getDepartmentId()).thenReturn("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        when(departmentHierarchyLevelSearchCriteria.getTenantId()).thenReturn("pb");
        Query actualBuildSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildSearchQuery(departmentHierarchyLevelSearchCriteria);
        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(4, actualBuildSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getDepartmentId();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getIds();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getLabel();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getLevel();
        verify(departmentHierarchyLevelSearchCriteria).getTenantId();
    }

    @Test
    void testBuildSearchQueryWithEmptyLabel() {
        DepartmentHierarchyLevelSearchCriteria departmentHierarchyLevelSearchCriteria = mock(
                DepartmentHierarchyLevelSearchCriteria.class);
        when(departmentHierarchyLevelSearchCriteria.getIds()).thenReturn(new ArrayList<>());
        when(departmentHierarchyLevelSearchCriteria.getLevel()).thenReturn(1);
        when(departmentHierarchyLevelSearchCriteria.getLabel()).thenReturn("");
        when(departmentHierarchyLevelSearchCriteria.getDepartmentId()).thenReturn("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        when(departmentHierarchyLevelSearchCriteria.getTenantId()).thenReturn("pb");
        Query actualBuildSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildSearchQuery(departmentHierarchyLevelSearchCriteria);
        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(3, actualBuildSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getDepartmentId();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getIds();
        verify(departmentHierarchyLevelSearchCriteria).getLabel();
        verify(departmentHierarchyLevelSearchCriteria, atLeast(1)).getLevel();
        verify(departmentHierarchyLevelSearchCriteria).getTenantId();
    }

    @Test
    void testBuildParentDeptHierarchyLevelSearchQuery() {
        Query actualBuildParentDeptHierarchyLevelSearchQueryResult = this.departmentHierarchyLevelQueryBuilder
                .buildParentDeptHierarchyLevelSearchQuery("7bdf9514-e2e5-4563-bfea-f5aaa41b2137", "pb", "Parent");
        assertFalse(actualBuildParentDeptHierarchyLevelSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildParentDeptHierarchyLevelSearchQueryResult.isSorted());
        assertTrue(actualBuildParentDeptHierarchyLevelSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(3, actualBuildParentDeptHierarchyLevelSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildParentDeptHierarchyLevelSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildParentDeptHierarchyLevelSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildParentDeptHierarchyLevelSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }*/
}

