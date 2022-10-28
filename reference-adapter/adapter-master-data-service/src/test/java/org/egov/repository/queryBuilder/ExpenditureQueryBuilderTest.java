package org.egov.repository.queryBuilder;

import org.egov.web.models.ExpenditureSearchCriteria;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ExpenditureQueryBuilderTest {
    @InjectMocks
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

    }
}

