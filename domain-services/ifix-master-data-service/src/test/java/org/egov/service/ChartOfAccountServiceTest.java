package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.repository.ChartOfAccountRepository;
import org.egov.tracer.model.CustomException;
import org.egov.validator.ChartOfAccountValidator;
import org.egov.web.models.COARequest;
import org.egov.web.models.COAResponse;
import org.egov.web.models.COASearchRequest;
import org.egov.web.models.ChartOfAccount;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest()
class ChartOfAccountServiceTest {

    @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ChartOfAccountValidator chartOfAccountValidator;

    @Mock
    private ChartOfAccountRepository chartOfAccountRepository;

    @Mock
    private COAEnrichmentService coaEnrichmentService;

    private COARequest coaRequest;
    private COASearchRequest coaSearchRequest;
    private COAResponse coaResponse;
    private COARequest headlessCoaRequest;

    @InjectMocks
    private ChartOfAccountService chartOfAccountService;

    @BeforeAll
    public void init() throws IOException {
        coaRequest = testDataFormatter.getCoaRequestData();
        headlessCoaRequest = testDataFormatter.getHeadlessCoaRequestData();
        coaSearchRequest = testDataFormatter.getCoaSearchRequestData();

        coaResponse = testDataFormatter.getCoaSearchResponseData();
    }

    @Test
    void chartOfAccountV1CreatePost() {
        doNothing().when(chartOfAccountValidator).validateCreatePost(coaRequest);
        doNothing().when(coaEnrichmentService).enrichCreatePost(coaRequest);
        doNothing().when(chartOfAccountRepository).save(coaRequest.getChartOfAccount());

        COARequest savedCoaRequest = chartOfAccountService.chartOfAccountV1CreatePost(coaRequest);

        assertEquals(savedCoaRequest, coaRequest);
    }

    @Test
    void chartOfAccountV1CreatePostException() {
        doThrow(new CustomException()).when(chartOfAccountValidator).validateCreatePost(headlessCoaRequest);

        assertThrows(CustomException.class,
                () -> chartOfAccountValidator.validateCreatePost(headlessCoaRequest));
    }

    @Test
    void chartOfAccountV1SearchPost() {
        doNothing().when(chartOfAccountValidator).validateSearchPost(coaSearchRequest);

        doReturn(coaResponse.getChartOfAccounts()).when(chartOfAccountRepository)
                .search(coaSearchRequest.getCriteria());

        List<ChartOfAccount> chartOfAccounts = chartOfAccountService.chartOfAccountV1SearchPost(coaSearchRequest);

        assertEquals(chartOfAccounts, coaResponse.getChartOfAccounts());
    }

    @Test
    void chartOfAccountV1SearchPostException() {
        doThrow(new CustomException()).when(chartOfAccountValidator).validateSearchPost(coaSearchRequest);

        assertThrows(CustomException.class,
                () -> chartOfAccountValidator.validateSearchPost(coaSearchRequest));
    }
}