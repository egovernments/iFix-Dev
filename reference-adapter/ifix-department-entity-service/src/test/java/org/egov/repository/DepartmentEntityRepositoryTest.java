package org.egov.repository;

import org.egov.config.TestDataFormatter;
import org.egov.repository.queryBuilder.DepartmentEntityQueryBuilder;
import org.egov.web.models.DepartmentEntity;
import org.egov.web.models.DepartmentEntityRequest;
import org.egov.web.models.DepartmentEntitySearchCriteria;
import org.egov.web.models.DepartmentEntitySearchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityRepositoryTest {

    @InjectMocks
    private DepartmentEntityRepository repository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private DepartmentEntityQueryBuilder queryBuilder;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentEntityRequest departmentEntityUpdateRequest;
    private DepartmentEntity updateDepartmentEntity;

    @BeforeAll
    void init() throws IOException {
        departmentEntityUpdateRequest = testDataFormatter.getDeptEntityUpdateRequestData();
        updateDepartmentEntity = departmentEntityUpdateRequest.getDepartmentEntity();
    }


    @Test
    void testSearchEntityWithEmptySearchCriteria() {
        DepartmentEntitySearchRequest departmentEntitySearchRequest = new DepartmentEntitySearchRequest();
        departmentEntitySearchRequest.setCriteria(new DepartmentEntitySearchCriteria());
        assertTrue(repository.searchEntity(departmentEntitySearchRequest).isEmpty());
    }

    @Test
    void testSearchEntityWithSearchCriteria() {
        DepartmentEntitySearchRequest departmentEntitySearchRequest = mock(DepartmentEntitySearchRequest.class);
        when(departmentEntitySearchRequest.getCriteria())
                .thenReturn(new DepartmentEntitySearchCriteria(new ArrayList<>(), "pb", "7bdf9514-e2e5-4563-bfea-f5aaa41b2137",
                        "Code", "Name", 1, true));
        assertTrue(repository.searchEntity(departmentEntitySearchRequest).isEmpty());
    }

    @Test
    void testGetParent() {
        when(mongoTemplate.find(any(), any(), any())).thenReturn(new ArrayList<>());
        assertNull(repository.getParent("7bdf9514-e2e5-4563-bfea-f5aaa41b2137"));
    }

    @Test
    void testGetParentWithDepartmentEntityList() {
        List<Object> departmentEntityList = new ArrayList<>();
        departmentEntityList.add(new DepartmentEntity());
        when(mongoTemplate.find(any(), any(), any())).thenReturn(departmentEntityList);
        assertNotNull(repository.getParent("7bdf9514-e2e5-4563-bfea-f5aaa41b2137"));
    }

    @Test
    void testSearchChildDepartment() {
        List<String> childIdList = new ArrayList<>();
        childIdList.add("7bdf9514-e2e5-4563-bfea-f5aaa41b2137");
        when(queryBuilder.buildChildrenValidationQuery(childIdList, 1)).thenReturn(Optional.empty());
        assertTrue(repository.searchChildDepartment(childIdList, 1).isEmpty());
    }

    @Test
    void testFindByID() {
        String id = updateDepartmentEntity.getId();

        when(mongoTemplate.findById(any(), any())).thenReturn(updateDepartmentEntity);

        Optional<DepartmentEntity> departmentEntityOptional = Optional.ofNullable(updateDepartmentEntity);

        assertEquals(departmentEntityOptional, repository.findById(id));
    }

}

