package org.egov.repository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class ExpenditureRepositoryTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ExpenditureQueryBuilder expenditureQueryBuilder;

    @InjectMocks
    private ExpenditureRepository expenditureRepository;

    private Query query;
    private ExpenditureRequest expenditureCreateRequest;
    private ExpenditureSearchRequest expenditureSearchRequest;
    private ExpenditureResponse expenditureSearchResponse;

    @BeforeAll
    public void init() throws IOException {
        query = new Query(Criteria.where("tenantId").is("pb"));

        expenditureCreateRequest = testDataFormatter.getExpenditureCreateRequestData();
        expenditureSearchRequest = testDataFormatter.getExpenditureSearchRequestData();
        expenditureSearchResponse = testDataFormatter.getExpenditureSearchResponseData();
    }

    @Test
    void testFindAllByCriteria() {
        doReturn(query).when(expenditureQueryBuilder).buildQuerySearch(expenditureSearchRequest.getCriteria());

        doReturn(expenditureSearchResponse.getExpenditure()).when(mongoTemplate).find(query, Expenditure.class);

        List<Expenditure> actualExpenditureList = expenditureRepository.findAllByCriteria(expenditureSearchRequest.getCriteria());

        assertSame(expenditureSearchResponse.getExpenditure(), actualExpenditureList);
    }

    @Test
    void testEmptyFindAllByCriteria() {
        List<Expenditure> emptyExpenditureList = new ArrayList<>();

        doReturn(new Query()).when(expenditureQueryBuilder).buildQuerySearch(any());

        doReturn(emptyExpenditureList).when(mongoTemplate).find(query, Expenditure.class);

        List<Expenditure> actualExpenditureList = expenditureRepository.findAllByCriteria(expenditureSearchRequest.getCriteria());

        assertNotEquals(expenditureSearchResponse.getExpenditure(), actualExpenditureList);
        assertThat(actualExpenditureList).isEmpty();
    }


    @Test
    void testSave() {
        assertNotNull(expenditureCreateRequest.getExpenditure());
        doReturn(expenditureCreateRequest.getExpenditure()).when(mongoTemplate)
                .save(expenditureCreateRequest.getExpenditure());

        expenditureRepository.save(expenditureCreateRequest.getExpenditure());

        verify(mongoTemplate).save(expenditureCreateRequest.getExpenditure());
    }*/
}