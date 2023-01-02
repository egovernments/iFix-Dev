package org.egov.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.entity.PspclBillDetail;
import org.egov.repository.builder.PspclIfixQueryBuilder;
import org.egov.repository.rowmapper.PspclBillRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclBillDetailRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PspclIfixQueryBuilder queryBuilder;

    @Autowired
    private PspclBillRowMapper billRowMapper;


    public int[] save(List<PspclBillDetail> pspclBillDetails) {
        List<SqlParameterSource> sqlParameterSources = queryBuilder.getPspclBillSqlParameterSources(pspclBillDetails);
        if (sqlParameterSources != null && !sqlParameterSources.isEmpty()) {
            return (namedParameterJdbcTemplate.batchUpdate(PspclIfixQueryBuilder.INSERT_QUERY_FOR_PSPCL_BILL_DETAIL,
                    sqlParameterSources.toArray(new SqlParameterSource[sqlParameterSources.size()])));
        }
        return new int[0];
    }

    public Optional<PspclBillDetail> findByBillIssueDateAndAccountNumber(Date dateReadingPrev, String accountNumber) {
        if (dateReadingPrev != null) {
            List<PspclBillDetail> pspclBillDetails = jdbcTemplate.query(queryBuilder.getPspclBillQueryForIssueDateAndAccountNumber(dateReadingPrev, accountNumber), billRowMapper);
            return (pspclBillDetails != null && !pspclBillDetails.isEmpty() ? Optional.of(pspclBillDetails.get(0)) : Optional.empty());
        }
        return Optional.empty();
    }

    public List<PspclBillDetail> findByORDERBYCOLUMNAndAccountNumber(String orderByColumn, String accountNumber) {
        List<PspclBillDetail> pspclBillDetails = new ArrayList<>();
        if (StringUtils.isNotBlank(orderByColumn)) {
            pspclBillDetails = jdbcTemplate.query(queryBuilder.getPspclBillQueryForOrderByColumnAndAccountNumber(orderByColumn, accountNumber), billRowMapper);
        }
        return pspclBillDetails;
    }

}
