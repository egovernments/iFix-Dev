package org.egov.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentHierarchyLevelEnrichmentServiceTest {

   /* @Mock
    private DepartmentEntityUtil departmentEntityUtil;

    @InjectMocks
    private DepartmentHierarchyLevelEnrichmentService departmentHierarchyLevelEnrichmentService;

    @Mock
    private DepartmentHierarchyLevelRepository_old departmentHierarchyLevelRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentHierarchyLevelRequest hierarchyLevelRequest;
    private AuditDetails auditDetails;

    @BeforeAll
    void init() throws IOException {
        hierarchyLevelRequest = testDataFormatter.getDeptHierarchyCreateRequestData();
        auditDetails = testDataFormatter.getDeptHierarchyCreateResponseData().getDepartmentHierarchyLevel().get(0).getAuditDetails();
    }

    @Test
    void testEnrichHierarchyLevelCreatePostWithEmptyResultForHierarchyLevel() {
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(new ArrayList<>());
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(new AuditDetails());

        assertThrows(CustomException.class, () -> this.departmentHierarchyLevelEnrichmentService
                .enrichHierarchyLevelCreatePost(hierarchyLevelRequest));
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any());
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
    }

    @Test
    void testEnrichHierarchyLevelCreatePostWithDefaultResultForHierarchyLevelSearch() {
        ArrayList<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        departmentHierarchyLevelList.add(new DepartmentHierarchyLevel());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(new AuditDetails());

        this.departmentHierarchyLevelEnrichmentService.enrichHierarchyLevelCreatePost(hierarchyLevelRequest);
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any());
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
    }

    @Test
    void testEnrichHierarchyLevelCreatePostWithSearchResultForHierarchyLevel() {
        ArrayList<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        departmentHierarchyLevelList
                .add(hierarchyLevelRequest.getDepartmentHierarchyLevel());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(new AuditDetails());

        this.departmentHierarchyLevelEnrichmentService.enrichHierarchyLevelCreatePost(hierarchyLevelRequest);
        verify(this.departmentHierarchyLevelRepository)
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any());
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
    }

    @Test
    void testEnrichHierarchyLevelCreatePostWithNullParent() {
        ArrayList<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        departmentHierarchyLevelList
                .add(new DepartmentHierarchyLevel("42", "42", "42", "Label", "Parent", 1, new AuditDetails()));
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(new AuditDetails());
        hierarchyLevelRequest.getDepartmentHierarchyLevel().setParent(null);
        this.departmentHierarchyLevelEnrichmentService.enrichHierarchyLevelCreatePost(hierarchyLevelRequest);
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
    }

    @Test
    void testEnrichHierarchyLevelCreatePostWithAuditDetails() {
        ArrayList<DepartmentHierarchyLevel> departmentHierarchyLevelList = new ArrayList<>();
        departmentHierarchyLevelList
                .add(hierarchyLevelRequest.getDepartmentHierarchyLevel());
        when(this.departmentHierarchyLevelRepository
                .searchDeptHierarchyLevel((org.egov.web.models.DepartmentHierarchyLevelSearchCriteria) any()))
                .thenReturn(departmentHierarchyLevelList);
        when(this.departmentEntityUtil.enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any()))
                .thenReturn(auditDetails);

        this.departmentHierarchyLevelEnrichmentService.enrichHierarchyLevelCreatePost(hierarchyLevelRequest);
        verify(this.departmentEntityUtil).enrichAuditDetails((String) any(), (AuditDetails) any(), (Boolean) any());
    }*/
}

