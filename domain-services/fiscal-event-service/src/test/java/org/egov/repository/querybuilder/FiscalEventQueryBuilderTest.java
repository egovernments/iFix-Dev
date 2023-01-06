/*
 * package org.egov.repository.querybuilder;
 *
 * import org.bson.Document; import org.egov.config.TestDataFormatter; import
 * org.egov.web.models.Criteria; import
 * org.egov.web.models.FiscalEventGetRequest; import
 * org.egov.web.models.FiscalEventResponse; import
 * org.junit.jupiter.api.BeforeAll; import org.junit.jupiter.api.Test; import
 * org.junit.jupiter.api.TestInstance; import org.mockito.InjectMocks; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.data.mongodb.core.query.Meta; import
 * org.springframework.data.mongodb.core.query.Query;
 *
 * import java.io.IOException;
 *
 * import static org.junit.jupiter.api.Assertions.*;
 *
 * @TestInstance(TestInstance.Lifecycle.PER_CLASS)
 *
 * @SpringBootTest class FiscalEventQueryBuilderTest {
 *
 * @InjectMocks private FiscalEventQueryBuilder fiscalEventQueryBuilder;
 *
 * @Autowired private TestDataFormatter testDataFormatter;
 *
 * private FiscalEventGetRequest fiscalEventGetRequest; private
 * FiscalEventResponse fiscalEventSearchResponse;
 *
 *
 * @BeforeAll void init() throws IOException { fiscalEventGetRequest =
 * testDataFormatter.getFiscalEventSearchRequestData();
 * fiscalEventSearchResponse =
 * testDataFormatter.getFiscalEventSearchResponseData(); }
 *
 *
 * @Test void testBuildSearchQueryWithEmptyCriteria() { Query
 * actualBuildSearchQueryResult =
 * this.fiscalEventQueryBuilder.buildSearchQuery(new Criteria());
 * assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
 * assertFalse(actualBuildSearchQueryResult.isSorted());
 * assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
 * assertEquals(1, actualBuildSearchQueryResult.getQueryObject().size());
 * Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
 * assertEquals(expectedFieldsObject,
 * actualBuildSearchQueryResult.getFieldsObject()); Meta meta =
 * actualBuildSearchQueryResult.getMeta(); assertNull(meta.getMaxTimeMsec());
 * assertTrue(meta.getFlags().isEmpty()); }
 *
 * @Test void testBuildSearchQueryWithCriteria() { Query
 * actualBuildSearchQueryResult = this.fiscalEventQueryBuilder
 * .buildSearchQuery(fiscalEventGetRequest.getCriteria());
 * assertFalse(actualBuildSearchQueryResult.getCollation().isPresent());
 * assertFalse(actualBuildSearchQueryResult.isSorted());
 * assertTrue(actualBuildSearchQueryResult.getRestrictedTypes().isEmpty());
 * assertTrue(actualBuildSearchQueryResult.getQueryObject().size() > 0);
 * Document expectedFieldsObject = actualBuildSearchQueryResult.getSortObject();
 * assertEquals(expectedFieldsObject,
 * actualBuildSearchQueryResult.getFieldsObject()); Meta meta =
 * actualBuildSearchQueryResult.getMeta(); assertNull(meta.getMaxTimeMsec());
 * assertTrue(meta.getFlags().isEmpty()); } }
 *
 */