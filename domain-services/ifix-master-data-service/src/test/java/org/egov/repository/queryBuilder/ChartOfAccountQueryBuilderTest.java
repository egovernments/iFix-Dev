///*
// * package org.egov.repository.queryBuilder;
// *
// * import org.bson.Document; import org.egov.config.TestDataFormatter; import
// * org.egov.web.models.COARequest; import org.egov.web.models.COAResponse;
// * import org.egov.web.models.COASearchCriteria; import
// * org.egov.web.models.COASearchRequest; import org.junit.jupiter.api.BeforeAll;
// * import org.junit.jupiter.api.Test; import org.junit.jupiter.api.TestInstance;
// * import org.mockito.InjectMocks; import
// * org.springframework.beans.factory.annotation.Autowired; import
// * org.springframework.boot.test.context.SpringBootTest; import
// * org.springframework.data.mongodb.core.query.Meta; import
// * org.springframework.data.mongodb.core.query.Query;
// *
// * import java.io.IOException;
// *
// * import static org.junit.jupiter.api.Assertions.*;
// *
// * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// *
// * @SpringBootTest() class ChartOfAccountQueryBuilderTest {
// *
// * @InjectMocks private ChartOfAccountQueryBuilder chartOfAccountQueryBuilder;
// *
// * @Autowired private TestDataFormatter testDataFormatter;
// *
// * private COARequest coaRequest; private COASearchRequest coaSearchRequest;
// * private COAResponse coaResponse; private COARequest headlessCoaRequest;
// *
// * @BeforeAll public void init() throws IOException { coaRequest =
// * testDataFormatter.getCoaRequestData(); headlessCoaRequest =
// * testDataFormatter.getHeadlessCoaRequestData(); coaSearchRequest =
// * testDataFormatter.getCoaSearchRequestData();
// *
// * coaResponse = testDataFormatter.getCoaSearchResponseData(); }
// *
// * @Test void testBuildSearchQuery() { Query actualBuildSearchQueryResult =
// * this.chartOfAccountQueryBuilder.buildSearchQuery(new COASearchCriteria());
// * assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
// * assertFalse(actualBuildSearchQueryResult.isSorted());
// * assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
// * assertEquals(1, actualBuildSearchQueryResult.getQueryObject().size());
// * Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
// * assertEquals(expectedFieldsObject,
// * actualBuildSearchQueryResult.getFieldsObject()); Meta meta =
// * actualBuildSearchQueryResult.getMeta(); assertNull(meta.getMaxTimeMsec());
// * assertTrue(meta.getFlags().isEmpty()); }
// *
// * @Test void testBuildSearchQueryWithSearchCriteria() { COASearchCriteria
// * coaSearchCriteria = coaSearchRequest.getCriteria(); Query
// * actualBuildSearchQueryResult = this.chartOfAccountQueryBuilder
// * .buildSearchQuery(coaSearchCriteria);
// * assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
// * assertFalse(actualBuildSearchQueryResult.isSorted());
// * assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
// * assertTrue(actualBuildSearchQueryResult.getQueryObject().size() > 0);
// * Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
// * assertEquals(expectedFieldsObject,
// * actualBuildSearchQueryResult.getFieldsObject()); Meta meta =
// * actualBuildSearchQueryResult.getMeta(); assertNull(meta.getMaxTimeMsec());
// * assertTrue(meta.getFlags().isEmpty()); } }
// *
// */