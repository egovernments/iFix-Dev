package org.egov.ifix.aggregate.processor;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.aggregate.model.FiscalEventAggregate;
import org.egov.ifix.aggregate.repository.FiscalEventAggregateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FiscalEventAggregateProcessor implements ApplicationRunner {

    @Autowired
    private DruidDataQueryProcessor druidDataQueryProcessor;

    @Autowired
    private FiscalEventAggregateRepository aggregateRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<FiscalEventAggregate> fiscalEventAggregateList = druidDataQueryProcessor.fetchFiscalEventFromDruid();
        //pass the list for upsert
        if (fiscalEventAggregateList != null && !fiscalEventAggregateList.isEmpty()) {
            int[] upsertedRecord = aggregateRepository.upsert(fiscalEventAggregateList);
            log.info("Record -> {} upserted successfully!", upsertedRecord != null ? upsertedRecord.length : 0);
        }
    }
}
