package org.egov.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.entity.PaymentJsonData;
import org.egov.entity.PspclPaymentDetail;
import org.egov.repository.builder.PspclIfixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclPaymentRowMapper implements ResultSetExtractor<List<PspclPaymentDetail>> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<PspclPaymentDetail> extractData(ResultSet rs) throws SQLException, DataAccessException {

        List<PspclPaymentDetail> pspclPaymentDetails = new ArrayList<>();

        while (rs.next()) {
            Long createdTime = rs.getLong(PspclIfixQueryBuilder.CREATEDTIME);
            Long lastModifiedTime = rs.getLong(PspclIfixQueryBuilder.LASTMODIFIEDTIME);
            String createdBy = rs.getString(PspclIfixQueryBuilder.CREATEDBY);
            String lastModifiedBy = rs.getString(PspclIfixQueryBuilder.LASTMODIFIEDBY);

            AuditDetails auditDetails = AuditDetails.builder()
                    .createdTime(createdTime)
                    .createdBy(createdBy)
                    .lastModifiedTime(lastModifiedTime)
                    .lastModifiedBy(lastModifiedBy)
                    .build();

            Object jsonData = rs.getObject(PspclIfixQueryBuilder.JSON_DATA);
            PaymentJsonData paymentJsonData = objectMapper.convertValue(jsonData, PaymentJsonData.class);

            PspclPaymentDetail pspclPaymentDetail = PspclPaymentDetail.builder()
                    .id(rs.getLong(PspclIfixQueryBuilder.ID))
                    .ACNO(rs.getString(PspclIfixQueryBuilder.ACNO))
                    .AMT(rs.getString(PspclIfixQueryBuilder.AMT))
                    .TXNDATE(rs.getDate(PspclIfixQueryBuilder.TXNDATE))
                    .TXNID(rs.getString(PspclIfixQueryBuilder.TXNID))
                    .BILCYC(rs.getString(PspclIfixQueryBuilder.BILCYC))
                    .BILGRP(rs.getString(PspclIfixQueryBuilder.BILGRP))
                    .BILNO(rs.getString(PspclIfixQueryBuilder.BILNO))
                    .BILISSDT(rs.getDate(PspclIfixQueryBuilder.BILISSDT))
                    .DUEDTCASH(rs.getDate(PspclIfixQueryBuilder.DUEDTCASH))
                    .DUEDTCHQ(rs.getDate(PspclIfixQueryBuilder.DUEDTCHQ))
                    .STATUS_P(rs.getString(PspclIfixQueryBuilder.STATUS_P))
                    .paymentJsonData(paymentJsonData)
                    .auditDetails(auditDetails)
                    .build();

            pspclPaymentDetails.add(pspclPaymentDetail);

        }

        return pspclPaymentDetails;
    }
}
