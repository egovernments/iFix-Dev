package org.egov.repository.queryBuilder;

import org.egov.web.models.ProjectSearchCriteria;
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
class ProjectQueryBuilderTest {

    @InjectMocks
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

    }
}

