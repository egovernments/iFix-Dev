package org.egov.repository;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.entity.PspclPaymentDetail;
import org.egov.repository.builder.PspclIfixQueryBuilder;
import org.egov.repository.rowmapper.PspclPaymentRowMapper;
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
public class PspclPaymentDetailRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PspclIfixQueryBuilder queryBuilder;

    @Autowired
    private PspclPaymentRowMapper paymentRowMapper;

    public int[] save(List<PspclPaymentDetail> pspclPaymentDetails) {
        List<SqlParameterSource> sqlParameterSources = queryBuilder.getPspclPaymentSqlParameterSources(pspclPaymentDetails);
        if (sqlParameterSources != null && !sqlParameterSources.isEmpty()) {
            return (namedParameterJdbcTemplate.batchUpdate(PspclIfixQueryBuilder.INSERT_QUERY_FOR_PSPCL_PAYMENT_DETAIL,
                    sqlParameterSources.toArray(new SqlParameterSource[sqlParameterSources.size()])));
        }
        return new int[0];
    }

    public List<PspclPaymentDetail> findByTXNDATEBetweenOrderByTXNDATEDescAndAccountNumber(Date fromDate, Date toDate, String accountNumber) {
        List<PspclPaymentDetail> pspclPaymentDetails = new ArrayList<>();
        if (fromDate != null && toDate != null) {
            pspclPaymentDetails = jdbcTemplate.query(queryBuilder.getPspclPaymentQueryForBetweenAndOrderByTxnDateAndAccountNumber(fromDate, toDate, accountNumber), paymentRowMapper);
        }
        return pspclPaymentDetails;
    }

    public Optional<PspclPaymentDetail> findByTXNIDAndAccountNumber(String txnId, String accountNumber) {
        if (StringUtils.isNotBlank(txnId)) {
            List<PspclPaymentDetail> pspclPaymentDetails = jdbcTemplate.query(queryBuilder.getPspclPaymentQueryForTxnIdAndAccountNumber(txnId, accountNumber), paymentRowMapper);
            return (pspclPaymentDetails != null && !pspclPaymentDetails.isEmpty() ? Optional.of(pspclPaymentDetails.get(0)) : Optional.empty());
        }
        return Optional.empty();
    }
}
