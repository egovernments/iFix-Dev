package org.egov.repository.queryBuilder;

//import org.bson.Document;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.query.Meta;
//import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityQueryBuilderTest {

    @InjectMocks
    private DepartmentEntityQueryBuilder departmentEntityQueryBuilder;

    /*@Test
    void testBuildPlainSearchQueryWithDefaultSearchCriteria() {
        Query actualBuildPlainSearchQueryResult = this.departmentEntityQueryBuilder
                .buildPlainSearchQuery(new DepartmentEntitySearchCriteria());
        assertFalse(actualBuildPlainSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildPlainSearchQueryResult.isSorted());
        assertTrue(actualBuildPlainSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(1, actualBuildPlainSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildPlainSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildPlainSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildPlainSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildPlainSearchQueryWithSearchCriteria() {
        Query actualBuildPlainSearchQueryResult = this.departmentEntityQueryBuilder.buildPlainSearchQuery(
                new DepartmentEntitySearchCriteria(new ArrayList<>(), "pb", "2acf41fb-047d-4074-a8b9-cc49dc93fa15", "Code",
                        "Name", 1, true));
        assertFalse(actualBuildPlainSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildPlainSearchQueryResult.isSorted());
        assertTrue(actualBuildPlainSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(5, actualBuildPlainSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildPlainSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildPlainSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildPlainSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildPlainSearchQueryWithDepartmentEntityIds() {
        DepartmentEntitySearchCriteria departmentEntitySearchCriteria = new DepartmentEntitySearchCriteria();
        departmentEntitySearchCriteria.addIdsItem("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        Query actualBuildPlainSearchQueryResult = this.departmentEntityQueryBuilder
                .buildPlainSearchQuery(departmentEntitySearchCriteria);
        assertFalse(actualBuildPlainSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildPlainSearchQueryResult.isSorted());
        assertTrue(actualBuildPlainSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(2, actualBuildPlainSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildPlainSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildPlainSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildPlainSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildPlainSearchQueryWithEmptyDepartmentEntityIds() {
        DepartmentEntitySearchCriteria departmentEntitySearchCriteria = mock(DepartmentEntitySearchCriteria.class);
        when(departmentEntitySearchCriteria.getIds()).thenReturn(new ArrayList<>());
        when(departmentEntitySearchCriteria.getHierarchyLevel()).thenReturn(1);
        when(departmentEntitySearchCriteria.getName()).thenReturn("Name");
        when(departmentEntitySearchCriteria.getCode()).thenReturn("Code");
        when(departmentEntitySearchCriteria.getDepartmentId()).thenReturn("2acf41fb-047d-4074-a8b9-cc49dc93fa15");
        when(departmentEntitySearchCriteria.getTenantId()).thenReturn("pb");
        Query actualBuildPlainSearchQueryResult = this.departmentEntityQueryBuilder
                .buildPlainSearchQuery(departmentEntitySearchCriteria);
        assertFalse(actualBuildPlainSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildPlainSearchQueryResult.isSorted());
        assertTrue(actualBuildPlainSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(5, actualBuildPlainSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildPlainSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildPlainSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildPlainSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
        verify(departmentEntitySearchCriteria, atLeast(1)).getCode();
        verify(departmentEntitySearchCriteria, atLeast(1)).getDepartmentId();
        verify(departmentEntitySearchCriteria, atLeast(1)).getHierarchyLevel();
        verify(departmentEntitySearchCriteria, atLeast(1)).getIds();
        verify(departmentEntitySearchCriteria, atLeast(1)).getName();
        verify(departmentEntitySearchCriteria).getTenantId();
    }

    @Test
    void testBuildPlainSearchQueryWithEmptyDepartmentName() {
        DepartmentEntitySearchCriteria departmentEntitySearchCriteria = mock(DepartmentEntitySearchCriteria.class);
        when(departmentEntitySearchCriteria.getIds()).thenReturn(new ArrayList<>());
        when(departmentEntitySearchCriteria.getHierarchyLevel()).thenReturn(1);
        when(departmentEntitySearchCriteria.getName()).thenReturn("");
        when(departmentEntitySearchCriteria.getCode()).thenReturn("Code");
        when(departmentEntitySearchCriteria.getDepartmentId()).thenReturn("42");
        when(departmentEntitySearchCriteria.getTenantId()).thenReturn("42");
        Query actualBuildPlainSearchQueryResult = this.departmentEntityQueryBuilder
                .buildPlainSearchQuery(departmentEntitySearchCriteria);
        assertFalse(actualBuildPlainSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildPlainSearchQueryResult.isSorted());
        assertTrue(actualBuildPlainSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(4, actualBuildPlainSearchQueryResult.getQueryObject().size());
        Document expectedFieldsObject = actualBuildPlainSearchQueryResult.getSortObject();
        assertEquals(expectedFieldsObject, actualBuildPlainSearchQueryResult.getFieldsObject());
        Meta meta = actualBuildPlainSearchQueryResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
        verify(departmentEntitySearchCriteria, atLeast(1)).getCode();
        verify(departmentEntitySearchCriteria, atLeast(1)).getDepartmentId();
        verify(departmentEntitySearchCriteria, atLeast(1)).getHierarchyLevel();
        verify(departmentEntitySearchCriteria, atLeast(1)).getIds();
        verify(departmentEntitySearchCriteria).getName();
        verify(departmentEntitySearchCriteria).getTenantId();
    }

    @Test
    void testBuildChildrenValidationQueryWithEmptyChildrenList() {
        assertFalse(this.departmentEntityQueryBuilder.buildChildrenValidationQuery(new ArrayList<>(), 1).isPresent());
    }

    @Test
    void testBuildChildrenValidationQueryWithChildrenList() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        Optional<Query> actualBuildChildrenValidationQueryResult = this.departmentEntityQueryBuilder
                .buildChildrenValidationQuery(stringList, 1);
        assertTrue(actualBuildChildrenValidationQueryResult.isPresent());
        Query getResult = actualBuildChildrenValidationQueryResult.get();
        assertFalse(getResult.getCollation().isPresent());
        assertFalse(getResult.isSorted());
        assertTrue(getResult.getRestrictedTypes().isEmpty());
        assertEquals(2, getResult.getQueryObject().size());
        Document expectedFieldsObject = getResult.getSortObject();
        assertEquals(expectedFieldsObject, getResult.getFieldsObject());
        Meta meta = getResult.getMeta();
        assertNull(meta.getMaxTimeMsec());
        assertTrue(meta.getFlags().isEmpty());
    }

    @Test
    void testBuildChildrenValidationQueryWithNullHierarchyLevel() {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        assertFalse(this.departmentEntityQueryBuilder.buildChildrenValidationQuery(stringList, null).isPresent());
    }*/
}

