package org.egov.validator;

import com.google.gson.Gson;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.TestDataFormatter;
import org.egov.repository.ChartOfAccountRepository;
import org.egov.tracer.model.CustomException;
import org.egov.util.CoaUtil;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ChartOfAccountValidatorTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ChartOfAccountRepository chartOfAccountRepository;

    @InjectMocks
    private ChartOfAccountValidator chartOfAccountValidator;

    @Mock
    private CoaUtil coaUtil;

    private String coaCreateData;
    private String coaSearchData;
    private COARequest coaRequest;
    private COAResponse coaCreateResponse;
    private COASearchRequest coaSearchRequest;
    private COAResponse coaSearchResponse;


    @BeforeAll
    void init() throws IOException {
        coaRequest = testDataFormatter.getCoaRequestData();
        coaCreateResponse = testDataFormatter.getCoaCreateResponseData();

        coaCreateData = new Gson().toJson(coaRequest);

        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();
        coaSearchData = new Gson().toJson(coaSearchRequest);
        coaSearchResponse = testDataFormatter.getCoaSearchResponseData();

    }

    @Test
    void testValidateCreatePostWithEmptyRequest() {
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(new COARequest()));
    }

    @Test
    void testValidateCreatePostWithEmptyRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class,
                () -> this.chartOfAccountValidator.validateCreatePost(new COARequest(requestHeader, new ChartOfAccount())));
    }

    @Test
    void testValidateCreatePostWithHeaderAndCoaContent() {
        COARequest coaRequest1 = mock(COARequest.class);
        //new RequestHeader(1633332305597L, "1.0.0", "ek9d96e8-3b6b-4e36-9503-0f14a01af74n", new UserInfo(), "ek9d96e8-3b6b-4e36-9503-0f14a01af74n", "Signature")
        when(coaRequest1.getRequestHeader())
                .thenReturn(coaRequest.getRequestHeader());
        when(coaRequest1.getChartOfAccount()).thenReturn(new ChartOfAccount());
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest1));
        verify(coaRequest1).getChartOfAccount();
        verify(coaRequest1).getRequestHeader();
    }

    @Test
    void testValidateCreatePostWithoutUserInfoInHeader() {
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(new ChartOfAccount());
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutUserInfoUserId() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUuid("01234567-89AB-CDEF-FEDC-BA9876543210");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(userInfo);
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(new ChartOfAccount());
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutTenantId() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        chartOfAccount.setTenantId(null);
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(chartOfAccount);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutGroupHead() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        chartOfAccount.setGroupHead(null);
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(chartOfAccount);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutMajorHead() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        chartOfAccount.setMajorHead(null);
        chartOfAccount.setGroupHead("g");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(chartOfAccount);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutMinorHead() {
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        chartOfAccount.setMinorHead(null);
        chartOfAccount.setMajorHead("MjH");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(chartOfAccount);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCreatePostWithoutSubHead() {
        ChartOfAccount chartOfAccount = new ChartOfAccount();
        chartOfAccount.setSubHead(null);
        chartOfAccount.setMinorHead("Mn");
        chartOfAccount.setMajorHead("Mj");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COARequest coaRequest = mock(COARequest.class);
        when(coaRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaRequest.getChartOfAccount()).thenReturn(chartOfAccount);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCreatePost(coaRequest));
        verify(coaRequest).getChartOfAccount();
        verify(coaRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateCoaCode() {
        when(this.chartOfAccountRepository.search((COASearchCriteria) any())).thenReturn(new ArrayList<ChartOfAccount>());
        this.chartOfAccountValidator.validateCoaCode(new COASearchCriteria());
        verify(this.chartOfAccountRepository).search((COASearchCriteria) any());
    }

    @Test
    void testValidateCoaCodeWithExistingCoaCode() {
        List<ChartOfAccount> chartOfAccountList = coaSearchResponse.getChartOfAccounts();
        when(this.chartOfAccountRepository.search((COASearchCriteria) any())).thenReturn(chartOfAccountList);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateCoaCode(new COASearchCriteria()));
        verify(this.chartOfAccountRepository).search((COASearchCriteria) any());
    }

    @Test
    void testValidateSearchPostWithEmptyCOASearchRequest() {
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(new COASearchRequest()));
    }

    @Test
    void testValidateSearchPostWithEmptyHeader() {
        RequestHeader requestHeader = new RequestHeader();
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator
                .validateSearchPost(new COASearchRequest(requestHeader, new COASearchCriteria())));
    }

    @Test
    void testValidateSearchPostWithEmptySearchCriteria() {
        COASearchRequest coaSearchRequest1 = mock(COASearchRequest.class);
        when(coaSearchRequest1.getRequestHeader())
                .thenReturn(coaSearchRequest.getRequestHeader());
        when(coaSearchRequest1.getCriteria()).thenReturn(new COASearchCriteria());
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest1));
        verify(coaSearchRequest1).getCriteria();
        verify(coaSearchRequest1).getRequestHeader();
    }


    @Test
    void testValidateSearchPostWithSubMajorHeadWithLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setSubMajorHead("SubMajor Head");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithMajorHeadLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setMajorHead("Major Header");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithoutSubHeadLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setSubHead("Sub Head");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithCoaCodeLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setCoaCode("mh-smh-mh-sh-gh-ohqeqwuhewiiwerooierpopeooorpeoweo[wq[pe[o[ewiepoepropoepwoepowopopowpopowo");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithObjectHead() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setObjectHead("Object head");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithTenantIdLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setTenantId("p");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithGroupHeadLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setGroupHead("Group Head");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

    @Test
    void testValidateSearchPostWithMinorHeadLength() {
        COASearchCriteria coaSearchCriteria = coaSearchRequest.getCriteria();
        coaSearchCriteria.setMinorHead("Minor Head");
        RequestHeader requestHeader = mock(RequestHeader.class);
        when(requestHeader.getUserInfo()).thenReturn(new UserInfo());
        COASearchRequest coaSearchRequest = mock(COASearchRequest.class);
        when(coaSearchRequest.getRequestHeader()).thenReturn(requestHeader);
        when(coaSearchRequest.getCriteria()).thenReturn(coaSearchCriteria);
        assertThrows(CustomException.class, () -> this.chartOfAccountValidator.validateSearchPost(coaSearchRequest));
        verify(coaSearchRequest).getCriteria();
        verify(coaSearchRequest).getRequestHeader();
        verify(requestHeader, atLeast(1)).getUserInfo();
    }

}

