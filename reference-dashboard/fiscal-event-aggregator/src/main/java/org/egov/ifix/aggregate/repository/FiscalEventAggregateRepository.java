package org.egov.ifix.aggregate.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.aggregate.model.FiscalEventAggregate;
import org.egov.ifix.aggregate.repository.mapper.FiscalEventAggregateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class FiscalEventAggregateRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int[] upsert(List<FiscalEventAggregate> fiscalEventAggregates) {
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(fiscalEventAggregates.toArray());
        return (namedParameterJdbcTemplate.batchUpdate(FiscalEventAggregateQuery.UPSERT_QUERY_FOR_FISCAL_EVENT_AGGREGATE,
                batch));
    }
}
