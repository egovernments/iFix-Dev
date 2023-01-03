package org.egov.ifix.aggregate.processor;

import org.egov.ifix.aggregate.model.FiscalEventAggregate;
import org.egov.ifix.aggregate.repository.FiscalEventAggregateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Application;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
class FiscalEventAggregateProcessorTest {

    @Mock
    private DruidDataQueryProcessor druidDataQueryProcessor;

    @InjectMocks
    private FiscalEventAggregateProcessor fiscalEventAggregateProcessor;

    @Mock
    private FiscalEventAggregateRepository fiscalEventAggregateRepository;

    @Test
    void testRunWithEmptyFiscalEvents() throws Exception {
        when(this.druidDataQueryProcessor.fetchFiscalEventFromDruid()).thenReturn(new ArrayList<>());
        this.fiscalEventAggregateProcessor.run(new DefaultApplicationArguments("foo", "foo", "foo"));
        verify(this.druidDataQueryProcessor, atLeast(1)).fetchFiscalEventFromDruid();
    }

    @Test
    void testRunWithUpsertResult() throws Exception {
        when(this.fiscalEventAggregateRepository.upsert((List<FiscalEventAggregate>) any()))
                .thenReturn(new int[]{1, 1, 1, 1});

        ArrayList<FiscalEventAggregate> fiscalEventAggregateList = new ArrayList<>();
        fiscalEventAggregateList.add(new FiscalEventAggregate());
        when(this.druidDataQueryProcessor.fetchFiscalEventFromDruid()).thenReturn(fiscalEventAggregateList);
        this.fiscalEventAggregateProcessor.run(new DefaultApplicationArguments("foo", "foo", "foo"));
        verify(this.fiscalEventAggregateRepository, atLeast(1)).upsert((List<FiscalEventAggregate>) any());
        verify(this.druidDataQueryProcessor, atLeast(1)).fetchFiscalEventFromDruid();
    }

    @Test
    void testRunWithNullUpsert() throws Exception {
        when(this.fiscalEventAggregateRepository.upsert((List<FiscalEventAggregate>) any())).thenReturn(null);

        ArrayList<FiscalEventAggregate> fiscalEventAggregateList = new ArrayList<>();
        fiscalEventAggregateList.add(new FiscalEventAggregate());
        when(this.druidDataQueryProcessor.fetchFiscalEventFromDruid()).thenReturn(fiscalEventAggregateList);
        this.fiscalEventAggregateProcessor.run(new DefaultApplicationArguments("foo", "foo", "foo"));
        verify(this.fiscalEventAggregateRepository).upsert((List<FiscalEventAggregate>) any());
        verify(this.druidDataQueryProcessor).fetchFiscalEventFromDruid();
    }
}

