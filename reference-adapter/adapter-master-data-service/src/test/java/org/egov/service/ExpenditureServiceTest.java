package org.egov.service;

import org.egov.config.TestDataFormatter;
import org.egov.repository.ExpenditureRepository;
import org.egov.validator.ExpenditureValidator;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest
class ExpenditureServiceTest {
 /*   @Autowired
    private TestDataFormatter testDataFormatter;

    @Mock
    private ExpenditureEnrichmentService expenditureEnrichmentService;

    @Mock
    private ExpenditureRepository expenditureRepository;

    @Mock
    private ExpenditureValidator expenditureValidator;

    @InjectMocks
    private ExpenditureService expenditureService;

    private ExpenditureSearchRequest expenditureSearchRequest;
    private ExpenditureResponse expenditureResponse;
    private ExpenditureRequest expenditureRequest;

    @BeforeAll
    public void init() throws IOException {
        expenditureSearchRequest = testDataFormatter.getExpenditureSearchRequestData();
        expenditureResponse = testDataFormatter.getExpenditureSearchResponseData();

        expenditureRequest = testDataFormatter.getExpenditureCreateRequestData();
    }

    @Test
    void testFindAllByCriteriaWithEmptySearchCriteria() {
        List<ExpenditureDTO> expenditureDTOList = new ArrayList();

        doNothing().when(expenditureValidator).validateExpenditureSearchRequest((ExpenditureSearchRequest) any());
        when(expenditureRepository.findAllByCriteria((ExpenditureSearchCriteria) any())).thenReturn(expenditureDTOList);

        List<ExpenditureDTO> actualFindAllByCriteriaResult = expenditureService
                .findAllByCriteria(new ExpenditureSearchRequest());

        assertSame(expenditureDTOList, actualFindAllByCriteriaResult);
        assertTrue(actualFindAllByCriteriaResult.isEmpty());

        verify(expenditureValidator).validateExpenditureSearchRequest((ExpenditureSearchRequest) any());
        verify(expenditureRepository).findAllByCriteria((ExpenditureSearchCriteria) any());
    }

    @Test
    void testFindAllByCriteria() {
        List<ExpenditureDTO> expenditureDTOList = expenditureResponse.getExpenditureDTO();
        assertNotNull(expenditureDTOList);

        doNothing().when(expenditureValidator).validateExpenditureSearchRequest(expenditureSearchRequest);
        doReturn(expenditureDTOList).when(expenditureRepository).findAllByCriteria(expenditureSearchRequest.getCriteria());

        List<ExpenditureDTO> actualFindAllByCriteriaResult = expenditureService
                .findAllByCriteria(expenditureSearchRequest);

        assertSame(expenditureDTOList, actualFindAllByCriteriaResult);

        verify(expenditureValidator).validateExpenditureSearchRequest((expenditureSearchRequest));
        verify(expenditureRepository).findAllByCriteria(expenditureSearchRequest.getCriteria());
    }

    @Test
    void testCreateV1ExpenditureWithEmptyRequestData() {
        doNothing().when(expenditureValidator).validateExpenditureCreateRequest((ExpenditureRequest) any());
        doNothing().when(expenditureRepository).save((ExpenditureDTO) any());
        doNothing().when(expenditureEnrichmentService).enrichCreateExpenditure((ExpenditureRequest) any());

        ExpenditureRequest expenditureRequest = expenditureService.createV1Expenditure(new ExpenditureRequest());
        assertNull(expenditureRequest.getExpenditureDTO());

        verify(expenditureValidator).validateExpenditureCreateRequest((ExpenditureRequest) any());
        verify(expenditureRepository).save((ExpenditureDTO) any());
        verify(expenditureEnrichmentService).enrichCreateExpenditure((ExpenditureRequest) any());
    }

    @Test
    void testCreateV1Expenditure() {
        doNothing().when(expenditureValidator).validateExpenditureCreateRequest(expenditureRequest);
        doNothing().when(expenditureRepository).save(expenditureRequest.getExpenditureDTO());
        doNothing().when(expenditureEnrichmentService).enrichCreateExpenditure(expenditureRequest);


        ExpenditureRequest actualExpenditureRequest = expenditureService.createV1Expenditure(expenditureRequest);
        assertSame(expenditureRequest, actualExpenditureRequest);

        verify(expenditureValidator).validateExpenditureCreateRequest(expenditureRequest);
        verify(expenditureRepository).save(expenditureRequest.getExpenditureDTO());
        verify(expenditureEnrichmentService).enrichCreateExpenditure(expenditureRequest);
    }*/
}

