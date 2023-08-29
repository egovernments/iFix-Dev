package org.egov.util;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class MasterDepartmentUtilTest {

    /*@Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private MasterDataServiceConfiguration configuration;

    @Mock
    private ServiceRequestRepository searchRequestRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MasterDepartmentUtil masterDepartmentUtil;

    private ArrayList<String> idList;
    private RequestHeader requestHeader = new RequestHeader();
    private List<DepartmentConst> departmentList;
    private Object departmentSearchResponseObject;
    private DepartmentResponse departmentResponse;

    @BeforeAll
    public void init() throws IOException {
        idList = new ArrayList<>();
        idList.add("DEPARTME-NT0B-CDEF-FEDC-ENTITY0UID10");

        departmentSearchResponseObject = testDataFormatter.getDepartmentSearchResponseDataAsObject();

        departmentResponse = testDataFormatter.getDepartmentSearchResponseData();

        departmentList = departmentResponse.getDepartment();

        doReturn(new String()).when(configuration).getIfixMasterDepartmentHost();
        doReturn(new String()).when(configuration).getIfixMasterDepartmentContextPath();
        doReturn(new String()).when(configuration).getIfixMasterDepartmentSearchPath();

    }


    @Test
    void testFetchDepartment() {
        doReturn(departmentSearchResponseObject).when(searchRequestRepository).fetchResult((String) any(), any());

        doReturn(departmentList).when(objectMapper).convertValue((Object) any(), (TypeReference) any());

        List<DepartmentConst> actualDepartmentList = masterDepartmentUtil.fetchDepartment(idList, "pb", requestHeader);

        assertSame(actualDepartmentList, departmentList);
    }

    @Test
    void testFalseFetchDepartment() {
        List<DepartmentConst> actualDepartmentList = masterDepartmentUtil
                .fetchDepartment(null, null, null);

        assertSame(Collections.emptyList(), actualDepartmentList);
    }


    @Test
    void testFetchDepartmentException() {
        doReturn(new Object()).when(searchRequestRepository).fetchResult((String) any(), any());

        assertThrows(CustomException.class,
                () -> masterDepartmentUtil.fetchDepartment(idList, "pb", requestHeader));
    }
*/
}

