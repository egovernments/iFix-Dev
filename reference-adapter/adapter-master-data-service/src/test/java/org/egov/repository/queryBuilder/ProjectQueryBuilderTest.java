package org.egov.repository.queryBuilder;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ProjectQueryBuilderTest {

   /* @InjectMocks
    private ProjectQueryBuilder projectQueryBuilder;

    private ArrayList<String> idList;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("PROJECT0-URE0-CDEF-FEDC-BUILDER00010");
    }

    @Test
    void testBuildQuerySearch2() {
        Query actualBuildQuerySearchResult = projectQueryBuilder
                .buildQuerySearch(new ProjectSearchCriteria(idList, "42", "Name", "Code",
                        "42", "42", "42"));
        assertFalse(actualBuildQuerySearchResult.getCollation().isPresent());
        assertFalse(actualBuildQuerySearchResult.isSorted());
        assertTrue(actualBuildQuerySearchResult.getRestrictedTypes().isEmpty());
        assertEquals(7, actualBuildQuerySearchResult.getQueryObject().size());

    }*/
}

