package org.egov.repository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class DepartmentRepositoryTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private DepartmentQueryBuilder departmentQueryBuilder;

    @InjectMocks
    private DepartmentRepository departmentRepository;

    private DepartmentRequest departmentCreateRequest;
    private DepartmentSearchRequest departmentSearchRequest;
    private DepartmentResponse departmentSearchResponse;
    private Query query;


    @BeforeAll
    public void init() throws IOException {
        query = new Query(Criteria.where("tenantId").is("pb"));

        departmentCreateRequest = testDataFormatter.getDepartmentCreateRequestData();
        departmentSearchRequest = testDataFormatter.getDepartmentSearchRequestData();
        departmentSearchResponse = testDataFormatter.getDepartmentSearchResponseData();
    }

    @Test
    void testSearch() {
        doReturn(query).when(departmentQueryBuilder).buildSearchQuery(departmentSearchRequest.getCriteria());

        doReturn(departmentSearchResponse.getDepartment()).when(mongoTemplate).find(query, DepartmentConst.class);

        List<DepartmentConst> actualDepartmentList = departmentRepository.search(departmentSearchRequest.getCriteria());

        assertSame(departmentSearchResponse.getDepartment(), actualDepartmentList);
    }

    @Test
    void testEmptySearch() {
        List<DepartmentConst> emptyDepartmentList = new ArrayList<>();

        doReturn(new Query()).when(departmentQueryBuilder).buildSearchQuery(any());

        doReturn(emptyDepartmentList).when(mongoTemplate).find(new Query(), DepartmentConst.class);

        List<DepartmentConst> actualDepartmentList = departmentRepository.search(departmentSearchRequest.getCriteria());

        assertNotEquals(departmentSearchResponse.getDepartment(), actualDepartmentList);
        assertThat(actualDepartmentList).isEmpty();
    }

    @Test
    void testSave() {
        assertNotNull(departmentCreateRequest.getDepartment());
        doReturn(departmentCreateRequest.getDepartment()).when(mongoTemplate)
                .save(departmentCreateRequest.getDepartment());

        departmentRepository.save(departmentCreateRequest.getDepartment());

        verify(mongoTemplate).save(departmentCreateRequest.getDepartment());
    }*/
}