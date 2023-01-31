package org.egov.repository.queryBuilder;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ExpenditureQueryBuilderTest {
/*    @InjectMocks
    private ExpenditureQueryBuilder expenditureQueryBuilder;

    private ArrayList<String> idList;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("EXPENDIT-URE0-CDEF-FEDC-BUILDER00010");
    }

    @Test
    void testBuildQuerySearch() {
        Query actualBuildQuerySearchResult = expenditureQueryBuilder
                .buildQuerySearch(new ExpenditureSearchCriteria(idList, "42", "Name", "Code"));

        assertFalse(actualBuildQuerySearchResult.getCollation().isPresent());
        assertFalse(actualBuildQuerySearchResult.isSorted());
        assertTrue(actualBuildQuerySearchResult.getRestrictedTypes().isEmpty());
        assertEquals(4, actualBuildQuerySearchResult.getQueryObject().size());

    }*/
}

