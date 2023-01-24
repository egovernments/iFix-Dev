package org.egov.validator;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DepartmentValidatorTest {
/*    @Autowired
    private TestDataFormatter testDataFormatter;

    @InjectMocks
    private DepartmentValidator departmentValidator;

    private DepartmentRequest departmentCreateRequest;
    private DepartmentSearchRequest departmentSearchRequest;

    @BeforeEach
    public void setup() throws IOException {
        departmentCreateRequest = testDataFormatter.getDepartmentCreateRequestData();
        departmentSearchRequest = testDataFormatter.getDepartmentSearchRequestData();
    }

    @Test
    void testValidateSearchPostRequestHeaderException() {
        assertThrows(CustomException.class,
                () -> departmentValidator.validateSearchPost(new DepartmentSearchRequest()),
                "Request header is missing");
    }

    @Test
    void testValidateSearchPostSearchCriteriaException() {
        departmentSearchRequest.setCriteria(null);

        assertThrows(CustomException.class, () -> departmentValidator.validateSearchPost(departmentSearchRequest),
                "Search criteria is missing");
    }

    @Test
    void testValidateSearchPostTenantIdLengthException() {
        departmentSearchRequest.getCriteria().setTenantId("0");

        assertThrows(CustomException.class,
                () -> departmentValidator.validateSearchPost(departmentSearchRequest),
                "Tenant id's length is invalid");
    }

    @Test
    void testValidateSearchPostCriteriaNameLengthException() {
        departmentSearchRequest.getCriteria().setName("0");

        assertThrows(CustomException.class,
                () -> departmentValidator.validateSearchPost(departmentSearchRequest),
                "DepartmentConst name's length is invalid");
    }

    @Test
    void testValidateSearchPost4() {
        DepartmentSearchRequest departmentSearchRequest = mock(DepartmentSearchRequest.class);
        when(departmentSearchRequest.getRequestHeader())
                .thenReturn(new RequestHeader(1L, "1.0.2", "42", new UserInfo(), "42", "Signature"));
        when(departmentSearchRequest.getCriteria()).thenReturn(new DepartmentSearchCriteria());

        assertThrows(CustomException.class, () -> departmentValidator.validateSearchPost(departmentSearchRequest));
        verify(departmentSearchRequest).getCriteria();
        verify(departmentSearchRequest).getRequestHeader();
    }

    @Test
    void testValidateCreateRequestDataUserInfoException() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> departmentValidator
                .validateCreateRequestData(new DepartmentRequest(requestHeader, new DepartmentConst())));
    }

    @Test
    void testValidateCreateRequestDataParentException() {
        departmentCreateRequest.getDepartment().setParent("0");

        assertThrows(CustomException.class,
                () -> departmentValidator.validateCreateRequestData(departmentCreateRequest),
                "DepartmentConst parent length is invalid.");
    }*/

}

