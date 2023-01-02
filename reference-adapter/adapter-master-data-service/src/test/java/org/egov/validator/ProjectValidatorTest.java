package org.egov.validator;

import org.egov.common.contract.request.RequestHeader;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ProjectRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.DepartmentUtil;
import org.egov.util.ExpenditureUtil;
import org.egov.web.models.Project;
import org.egov.web.models.ProjectRequest;
import org.egov.web.models.ProjectSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ProjectValidatorTest {
    @MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private DepartmentUtil departmentUtil;

    @Mock
    private ExpenditureUtil expenditureUtil;

    @InjectMocks
    private ProjectValidator projectValidator;

    private ProjectSearchRequest projectSearchRequest;
    private ProjectRequest projectCreateRequest;
    private ProjectRequest projectUpdateRequest;

    @BeforeEach
    public void setup() throws IOException {
        projectSearchRequest = testDataFormatter.getProjectSearchRequestData();
        projectCreateRequest = testDataFormatter.getProjectCreateRequestData();
        projectUpdateRequest = testDataFormatter.getProjectUpdateRequestData();
    }

//    @Test
//    void testValidateProjectSearchRequestUserInfoException() {
//        projectSearchRequest.getRequestHeader().setUserInfo(null);
//
//        assertThrows(CustomException.class,
//                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
//                "User information is missing");
//    }

    @Test
    void testValidateProjectSearchRequestTenantIdException() {
        projectSearchRequest.getCriteria().setTenantId(null);

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Tenant id is missing in request data");
    }

    @Test
    void testValidateProjectSearchRequestTenantIdLengthException() {
        projectSearchRequest.getCriteria().setTenantId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Tenant id length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestNameLengthException() {
        projectSearchRequest.getCriteria().setName("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Project name length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestExpenditureIdLengthException() {
        projectSearchRequest.getCriteria().setExpenditureId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Expenditure id length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestDepartmentIdMinLengthException() {
        projectSearchRequest.getCriteria().setDepartmentEntityId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Department id length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestDepartmentIdMaxLengthException() {
        projectSearchRequest.getCriteria()
                .setDepartmentEntityId("6413d1d9ef49fbc4b6dac524c5e92d487b010464abd89839e4e7591d100f41a6a");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Department id length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestLocationIdMinLengthException() {
        projectSearchRequest.getCriteria().setLocationId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Location id length is invalid.");
    }

    @Test
    void testValidateProjectSearchRequestLocationIdMaxLengthException() {
        projectSearchRequest.getCriteria()
                .setLocationId("6413d1d9ef49fbc4b6dac524c5e92d487b010464abd89839e4e7591d100f41a6a");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectSearchRequest(projectSearchRequest),
                "Location id length is invalid.");
    }

    @Test
    void testValidateProjectCreateRequestPayloadException() {
        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(null),
                "Request payload is missing some value");
    }

    @Test
    void testValidateProjectCreateRequestUserInfoException() {
        projectCreateRequest.getRequestHeader().setUserInfo(null);

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "User information is missing.");
    }

    @Test
    void testValidateProjectCreateRequestTenantIdException() {
        projectCreateRequest.getProject().setTenantId(null);

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "User information is missing");
    }

    @Test
    void testValidateProjectCreateRequestTenantIdLengthException() {
        projectCreateRequest.getProject().setTenantId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "Tenant id length is invalid.");
    }

    @Test
    void testValidateProjectCreateRequestCodeException() {
        projectCreateRequest.getProject().setCode(null);

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "Project code is missing in request data");
    }

    @Test
    void testValidateProjectCreateRequestNameException() {
        projectCreateRequest.getProject().setName(null);

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "Project name is missing in request data");
    }

    @Test
    void testValidateProjectCreateRequestExpenditureIdLengthException() {
        projectCreateRequest.getProject().setExpenditureId("0");

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "Expenditure id length is invalid. Length range [2-64]");
    }

    @Test
    void testValidateProjectCreateRequestDepartmentEntityIdLengthException() {
        projectCreateRequest.getProject().setDepartmentEntityIds(Collections.singletonList("0"));

        assertThrows(CustomException.class,
                () -> projectValidator.validateProjectCreateRequest(projectCreateRequest),
                "Department Entity id length is invalid.");
    }

    @Test
    void testValidateProjectUpdateRequestWithDefaultRequest() {
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(new ProjectRequest()));
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(null));
    }

    @Test
    void testValidateProjectUpdateRequestWithRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class,
                () -> this.projectValidator.validateProjectUpdateRequest(new ProjectRequest(requestHeader, new Project())));
    }

    @Test
    void testValidateProjectUpdateRequestWithDefaultRequestAndHeader() {
        ProjectRequest projectRequest = mock(ProjectRequest.class);
        when(projectRequest.getRequestHeader()).thenReturn(new RequestHeader());
        when(projectRequest.getProject()).thenReturn(new Project());
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(projectRequest));
        verify(projectRequest, atLeast(1)).getRequestHeader();
        verify(projectRequest, atLeast(1)).getProject();
    }

    @Test
    void testValidateProjectUpdateRequestWithMissingRequestHeader() {
        ProjectRequest projectRequest = mock(ProjectRequest.class);
        when(projectRequest.getRequestHeader()).thenThrow(new CustomException("REQUEST_HEADER", "Request header is missing!"));
        when(projectRequest.getProject()).thenReturn(new Project());
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(projectRequest));
        verify(projectRequest).getRequestHeader();
        verify(projectRequest).getProject();
    }

    @Test
    void testValidateProjectUpdateRequestWithNullHeader() {
        ProjectRequest projectRequest = mock(ProjectRequest.class);
        when(projectRequest.getRequestHeader()).thenReturn(null);
        when(projectRequest.getProject()).thenReturn(new Project());
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(projectRequest));
        verify(projectRequest).getRequestHeader();
        verify(projectRequest).getProject();
    }

    @Test
    void testValidateProjectUpdateRequestWithRequestHeaderValues() {
        ProjectRequest projectRequest = mock(ProjectRequest.class);
        when(projectRequest.getRequestHeader())
                .thenReturn(projectUpdateRequest.getRequestHeader());
        when(projectRequest.getProject()).thenReturn(new Project());
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(projectRequest));
        verify(projectRequest, atLeast(1)).getRequestHeader();
        verify(projectRequest, atLeast(1)).getProject();
    }

    @Test
    void testValidateProjectUpdateRequestWithProjectFindSearchResult() {
        when(this.projectRepository.findByProjectId((String) any())).thenReturn(Optional.of(projectUpdateRequest.getProject()));
        ProjectRequest projectRequest = mock(ProjectRequest.class);
        when(projectRequest.getRequestHeader()).thenReturn(new RequestHeader());
        ArrayList<String> departmentEntityIds = new ArrayList<>();
        ArrayList<String> locationIds = new ArrayList<>();
        when(projectRequest.getProject()).thenReturn(
                projectUpdateRequest.getProject());
        assertThrows(CustomException.class, () -> this.projectValidator.validateProjectUpdateRequest(projectRequest));
        verify(this.projectRepository).findByProjectId((String) any());
        verify(projectRequest, atLeast(1)).getRequestHeader();
        verify(projectRequest, atLeast(1)).getProject();
    }

}

