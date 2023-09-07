package org.egov.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DepartmentEnrichmentServiceTest {
   /* @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private DepartmentEnrichmentService departmentEnrichmentService;

    @Mock
    private MasterDataServiceUtil masterDataServiceUtil;

    private DepartmentRequest departmentRequest;
    private AuditDetails auditDetails;
    private String userId;

    @BeforeEach
    public void init() throws IOException {
        departmentRequest = testDataFormatter.getDepartmentCreateRequestData();

        DepartmentConst department = departmentRequest.getDepartment();
        RequestHeader requestHeader = departmentRequest.getRequestHeader();
        Long time = System.currentTimeMillis();
        userId = requestHeader.getUserInfo().getUuid();

        auditDetails = new AuditDetails(userId, userId, time, time);
    }

    @Test
    void testEnrichDepartmentDataWithAuditDetails() {
        departmentRequest.getDepartment().setAuditDetails(auditDetails);

        doReturn(auditDetails).when(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);

        departmentEnrichmentService.enrichDepartmentData(departmentRequest);

        assertNotNull(departmentRequest.getDepartment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, auditDetails, false);
    }

    @Test
    void testEnrichDepartmentDataWithoutAuditDetails() {
        doReturn(auditDetails).when(masterDataServiceUtil)
                .enrichAuditDetails(userId, departmentRequest.getDepartment().getAuditDetails(), true);

        departmentEnrichmentService.enrichDepartmentData(departmentRequest);

        assertNotNull(departmentRequest.getDepartment().getAuditDetails());
        verify(masterDataServiceUtil).enrichAuditDetails(userId, null, true);
    }

    @Test
    void testEnrichSearchPost() {
        DepartmentSearchRequest departmentSearchRequest = new DepartmentSearchRequest();
        departmentEnrichmentService.enrichSearchPost(departmentSearchRequest);
        assertNull(departmentSearchRequest.getCriteria());
        assertNull(departmentSearchRequest.getRequestHeader());
    }
*/
}

