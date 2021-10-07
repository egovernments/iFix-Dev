package org.egov.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.egov.config.TestDataFormatter;
import org.egov.repository.ExpenditureRepository;
import org.egov.validator.ExpenditureValidator;
import org.egov.web.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ExpenditureServiceTest {
    @Autowired
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
        List<Expenditure> expenditureList = new ArrayList();

        doNothing().when(expenditureValidator).validateExpenditureSearchRequest((ExpenditureSearchRequest) any());
        when(expenditureRepository.findAllByCriteria((ExpenditureSearchCriteria) any())).thenReturn(expenditureList);

        List<Expenditure> actualFindAllByCriteriaResult = expenditureService
                .findAllByCriteria(new ExpenditureSearchRequest());

        assertSame(expenditureList, actualFindAllByCriteriaResult);
        assertTrue(actualFindAllByCriteriaResult.isEmpty());

        verify(expenditureValidator).validateExpenditureSearchRequest((ExpenditureSearchRequest) any());
        verify(expenditureRepository).findAllByCriteria((ExpenditureSearchCriteria) any());
    }

    @Test
    void testFindAllByCriteria() {
        List<Expenditure> expenditureList = expenditureResponse.getExpenditure();
        assertNotNull(expenditureList);

        doNothing().when(expenditureValidator).validateExpenditureSearchRequest(expenditureSearchRequest);
        doReturn(expenditureList).when(expenditureRepository).findAllByCriteria(expenditureSearchRequest.getCriteria());

        List<Expenditure> actualFindAllByCriteriaResult = expenditureService
                .findAllByCriteria(expenditureSearchRequest);

        assertSame(expenditureList, actualFindAllByCriteriaResult);

        verify(expenditureValidator).validateExpenditureSearchRequest((expenditureSearchRequest));
        verify(expenditureRepository).findAllByCriteria(expenditureSearchRequest.getCriteria());
    }

    @Test
    void testCreateV1ExpenditureWithEmptyRequestData() {
        doNothing().when(expenditureValidator).validateExpenditureCreateRequest((ExpenditureRequest) any());
        doNothing().when(expenditureRepository).save((Expenditure) any());
        doNothing().when(expenditureEnrichmentService).enrichCreateExpenditure((ExpenditureRequest) any());

        ExpenditureRequest expenditureRequest = expenditureService.createV1Expenditure(new ExpenditureRequest());
        assertNull(expenditureRequest.getExpenditure());

        verify(expenditureValidator).validateExpenditureCreateRequest((ExpenditureRequest) any());
        verify(expenditureRepository).save((Expenditure) any());
        verify(expenditureEnrichmentService).enrichCreateExpenditure((ExpenditureRequest) any());
    }

    @Test
    void testCreateV1Expenditure() {
        doNothing().when(expenditureValidator).validateExpenditureCreateRequest(expenditureRequest);
        doNothing().when(expenditureRepository).save(expenditureRequest.getExpenditure());
        doNothing().when(expenditureEnrichmentService).enrichCreateExpenditure(expenditureRequest);


        ExpenditureRequest actualExpenditureRequest = expenditureService.createV1Expenditure(expenditureRequest);
        assertSame(expenditureRequest, actualExpenditureRequest);

        verify(expenditureValidator).validateExpenditureCreateRequest(expenditureRequest);
        verify(expenditureRepository).save(expenditureRequest.getExpenditure());
        verify(expenditureEnrichmentService).enrichCreateExpenditure(expenditureRequest);
    }
}

