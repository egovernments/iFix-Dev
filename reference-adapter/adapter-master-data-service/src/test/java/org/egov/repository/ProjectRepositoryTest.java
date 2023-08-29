package org.egov.repository;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class ProjectRepositoryTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private ProjectQueryBuilder projectQueryBuilder;

    @InjectMocks
    private ProjectRepository projectRepository;

    private Query query;
    private ProjectRequest projectCreateRequest;
    private ProjectRequest projectUpdateRequest;
    private ProjectSearchRequest projectSearchRequest;
    private ProjectResponse projectSearchResponse;

    @BeforeAll
    public void init() throws IOException {
        query = new Query(Criteria.where("tenantId").is("pb"));

        projectCreateRequest = testDataFormatter.getProjectCreateRequestData();
        projectSearchRequest = testDataFormatter.getProjectSearchRequestData();
        projectSearchResponse = testDataFormatter.getProjectSearchReponseData();
        projectUpdateRequest = testDataFormatter.getProjectUpdateRequestData();
    }

    @Test
    void testFindAllByCriteria() {
        doReturn(query).when(projectQueryBuilder).buildQuerySearch(projectSearchRequest.getCriteria());
        doReturn(projectSearchResponse.getProjectDTO()).when(mongoTemplate).find(query, ProjectDTO.class);

        List<ProjectDTO> actualProjectDTOList = projectRepository.findAllByCriteria(projectSearchRequest.getCriteria());

        assertSame(projectSearchResponse.getProjectDTO(), actualProjectDTOList);
    }

    @Test
    void testSave() {
        assertNotNull(projectCreateRequest.getProjectDTO());

        doReturn(projectCreateRequest.getProjectDTO()).when(mongoTemplate)
                .save(projectCreateRequest.getProjectDTO());

        projectRepository.save(projectCreateRequest.getProjectDTO());

        verify(mongoTemplate).save(projectCreateRequest.getProjectDTO());
    }

    @Test
    void testFindByProjectIdWithDefaultSearchResult() {
        when(this.mongoTemplate.findById((Object) any(), (Class<ProjectDTO>) any())).thenReturn(new ProjectDTO());
        assertTrue(this.projectRepository.findByProjectId("205180a0-4d60-4805-a77f-92143bd115b4").isPresent());
        verify(this.mongoTemplate).findById((Object) any(), (Class<ProjectDTO>) any());
    }

    @Test
    void testFindByProjectIdWithSearchResult() {
        when(this.mongoTemplate.findById((Object) any(), (Class<ProjectDTO>) any())).thenReturn(projectUpdateRequest.getProjectDTO());
        assertTrue(this.projectRepository.findByProjectId("205180a0-4d60-4805-a77f-92143bd115b4").isPresent());
        verify(this.mongoTemplate).findById((Object) any(), (Class<ProjectDTO>) any());
    }*/
}