package org.egov.repository;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.entity.EventPostingDetail;
import org.egov.repository.builder.PspclIfixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class EventPostingDetailRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PspclIfixQueryBuilder queryBuilder;

    public int[] save(List<EventPostingDetail> eventPostingDetailList) {
        List<SqlParameterSource> sqlParameterSources = queryBuilder.getEventPostingSqlParameterSources(eventPostingDetailList);
        if (sqlParameterSources != null && !sqlParameterSources.isEmpty()) {
            return (namedParameterJdbcTemplate.batchUpdate(PspclIfixQueryBuilder.INSERT_QUERY_FOR_EVENT_POSTING_DETAIL,
                    sqlParameterSources.toArray(new SqlParameterSource[sqlParameterSources.size()])));
        }
        return new int[0];
    }
}
