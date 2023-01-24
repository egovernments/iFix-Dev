package org.egov.service;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentServiceTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private DepartmentEnrichmentService departmentEnrichmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Mock
    private DepartmentValidator departmentValidator;

    private DepartmentRequest departmentRequest;
    private DepartmentSearchRequest departmentSearchRequest;
    private DepartmentResponse departmentSearchResponse;


    @BeforeAll
    public void init() throws IOException {
        departmentRequest = testDataFormatter.getDepartmentCreateRequestData();
        departmentSearchRequest = testDataFormatter.getDepartmentSearchRequestData();
        departmentSearchResponse = testDataFormatter.getDepartmentSearchResponseData();
    }

    @Test
    void testDepartmentV1SearchPostByEmptySearchCriteria() {
        doNothing().when(departmentValidator).validateSearchPost((DepartmentSearchRequest) any());
        ArrayList<DepartmentConst> departmentList = new ArrayList();
        when(departmentRepository.search((DepartmentSearchCriteria) any())).thenReturn(departmentList);
        doNothing().when(departmentEnrichmentService).enrichSearchPost((DepartmentSearchRequest) any());

        DepartmentSearchRequest departmentSearchRequest = new DepartmentSearchRequest();
        departmentSearchRequest.setCriteria(new DepartmentSearchCriteria());

        List<DepartmentConst> actualDepartmentV1SearchPostResult = departmentService
                .departmentV1SearchPost(departmentSearchRequest);

        assertSame(departmentList, actualDepartmentV1SearchPostResult);
        assertTrue(actualDepartmentV1SearchPostResult.isEmpty());

        verify(departmentValidator).validateSearchPost((DepartmentSearchRequest) any());
        verify(departmentRepository).search((DepartmentSearchCriteria) any());
        verify(departmentEnrichmentService).enrichSearchPost((DepartmentSearchRequest) any());
    }

    @Test
    void testDepartmentV1SearchPost() {
        doNothing().when(departmentValidator).validateSearchPost(departmentSearchRequest);
        doNothing().when(departmentEnrichmentService).enrichSearchPost(departmentSearchRequest);

        List<DepartmentConst> departmentList = departmentSearchResponse.getDepartment();
        when(departmentRepository.search((DepartmentSearchCriteria) any())).thenReturn(departmentList);

        List<DepartmentConst> actualDepartmentV1SearchPostResult = departmentService
                .departmentV1SearchPost(departmentSearchRequest);

        assertSame(departmentList, actualDepartmentV1SearchPostResult);
        assertEquals(1, actualDepartmentV1SearchPostResult.size());

        verify(departmentValidator).validateSearchPost((DepartmentSearchRequest) any());
        verify(departmentRepository).search((DepartmentSearchCriteria) any());
        verify(departmentEnrichmentService).enrichSearchPost((DepartmentSearchRequest) any());
    }


    @Test
    void testCreateDepartmentWithEmptyRequestData() {
        doNothing().when(departmentValidator).validateCreateRequestData((DepartmentRequest) any());
        doNothing().when(departmentRepository).save((DepartmentConst) any());
        doNothing().when(departmentEnrichmentService).enrichDepartmentData((DepartmentRequest) any());

        DepartmentRequest departmentRequest = departmentService.createDepartment(new DepartmentRequest());
        assertNull(departmentRequest.getDepartment());

        verify(departmentValidator).validateCreateRequestData((DepartmentRequest) any());
        verify(departmentRepository).save((DepartmentConst) any());
        verify(departmentEnrichmentService).enrichDepartmentData((DepartmentRequest) any());
    }

    @Test
    void testCreateDepartment() {
        doNothing().when(departmentValidator).validateCreateRequestData(departmentRequest);
        doNothing().when(departmentRepository).save(departmentRequest.getDepartment());
        doNothing().when(departmentEnrichmentService).enrichDepartmentData(departmentRequest);

        DepartmentRequest actualDepartmentRequest = departmentService.createDepartment(departmentRequest);

        assertEquals(departmentRequest, actualDepartmentRequest);

        verify(departmentValidator).validateCreateRequestData((DepartmentRequest) any());
        verify(departmentRepository).save((DepartmentConst) any());
        verify(departmentEnrichmentService).enrichDepartmentData((DepartmentRequest) any());
    }*/
}

