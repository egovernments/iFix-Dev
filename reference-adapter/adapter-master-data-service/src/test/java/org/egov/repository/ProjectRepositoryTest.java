package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.repository.queryBuilder.ProjectQueryBuilder;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectResponse;
import org.egov.web.models.ProjectSearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.List;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class ProjectRepositoryTest {
    @Autowired
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
        doReturn(projectSearchResponse.getProject()).when(mongoTemplate).find(query, Project.class);

        List<Project> actualProjectList = projectRepository.findAllByCriteria(projectSearchRequest.getCriteria());

        assertSame(projectSearchResponse.getProject(), actualProjectList);
    }

    @Test
    void testSave() {
        assertNotNull(projectCreateRequest.getProject());

        doReturn(projectCreateRequest.getProject()).when(mongoTemplate)
                .save(projectCreateRequest.getProject());

        projectRepository.save(projectCreateRequest.getProject());

        verify(mongoTemplate).save(projectCreateRequest.getProject());
    }

    @Test
    void testFindByProjectIdWithDefaultSearchResult() {
        when(this.mongoTemplate.findById((Object) any(), (Class<Project>) any())).thenReturn(new Project());
        assertTrue(this.projectRepository.findByProjectId("205180a0-4d60-4805-a77f-92143bd115b4").isPresent());
        verify(this.mongoTemplate).findById((Object) any(), (Class<Project>) any());
    }

    @Test
    void testFindByProjectIdWithSearchResult() {
        when(this.mongoTemplate.findById((Object) any(), (Class<Project>) any())).thenReturn(projectUpdateRequest.getProject());
        assertTrue(this.projectRepository.findByProjectId("205180a0-4d60-4805-a77f-92143bd115b4").isPresent());
        verify(this.mongoTemplate).findById((Object) any(), (Class<Project>) any());
    }
}