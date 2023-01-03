package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.repository.queryBuilder.DepartmentQueryBuilder;
import org.egov.web.models.Department;
import org.egov.web.models.DepartmentRequest;
import org.egov.web.models.DepartmentResponse;
import org.egov.web.models.DepartmentSearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class DepartmentRepositoryTest {
    @Autowired
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

        doReturn(departmentSearchResponse.getDepartment()).when(mongoTemplate).find(query, Department.class);

        List<Department> actualDepartmentList = departmentRepository.search(departmentSearchRequest.getCriteria());

        assertSame(departmentSearchResponse.getDepartment(), actualDepartmentList);
    }

    @Test
    void testEmptySearch() {
        List<Department> emptyDepartmentList = new ArrayList<>();

        doReturn(new Query()).when(departmentQueryBuilder).buildSearchQuery(any());

        doReturn(emptyDepartmentList).when(mongoTemplate).find(new Query(), Department.class);

        List<Department> actualDepartmentList = departmentRepository.search(departmentSearchRequest.getCriteria());

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
    }
}