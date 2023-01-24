package org.egov.repository.queryBuilder;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentQueryBuilderTest {
  /*  @InjectMocks
    private DepartmentQueryBuilder departmentQueryBuilder;

    private ArrayList<String> idList;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");
    }

    @Test
    void testBuildSearchQuery() {
        Query actualBuildSearchQueryResult = this.departmentQueryBuilder
                .buildSearchQuery(new DepartmentSearchCriteria(idList, "42", "Name", "Code"));

        assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
        assertFalse(actualBuildSearchQueryResult.isSorted());
        assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
        assertEquals(4, actualBuildSearchQueryResult.getQueryObject().size());
    }*/
}

