package org.egov.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityRepositoryTest {
/*

    @InjectMocks
    private DepartmentEntityRepository_old repository;

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
*/

}

