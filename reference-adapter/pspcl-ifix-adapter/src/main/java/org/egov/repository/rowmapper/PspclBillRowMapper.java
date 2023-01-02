package org.egov.repository.rowmapper;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.AuditDetails;
import org.egov.entity.BillJsonData;
import org.egov.entity.PspclBillDetail;
import org.egov.repository.builder.PspclIfixQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclBillRowMapper implements ResultSetExtractor<List<PspclBillDetail>> {

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public List<PspclBillDetail> extractData(ResultSet rs) throws SQLException, DataAccessException {

        List<PspclBillDetail> pspclBillDetails = new LinkedList<>();

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
            BillJsonData billJsonData = objectMapper.convertValue(jsonData, BillJsonData.class);


            PspclBillDetail pspclBillDetail = PspclBillDetail.builder()
                    .id(rs.getLong(PspclIfixQueryBuilder.ID))
                    .ACCOUNT_NO(rs.getString(PspclIfixQueryBuilder.ACCOUNT_NO))
                    .BILL_NO(rs.getString(PspclIfixQueryBuilder.BILL_NO))
                    .BILL_ISSUE_DATE(rs.getDate(PspclIfixQueryBuilder.BILL_ISSUE_DATE))
                    .DATE_READING_CURR(rs.getDate(PspclIfixQueryBuilder.DATE_READING_CURR))
                    .DATE_READING_PREV(rs.getDate(PspclIfixQueryBuilder.DATE_READING_PREV))
                    .DUE_DATE_CASH_ONLINE(rs.getString(PspclIfixQueryBuilder.DUE_DATE_CASH_ONLINE))
                    .DUE_DATE_CHEQUE_DD(rs.getString(PspclIfixQueryBuilder.DUE_DATE_CHEQUE_DD))
                    .PAYABLE_AMOUNT_BY_DUE_DATE(rs.getString(PspclIfixQueryBuilder.PAYABLE_AMOUNT_BY_DUE_DATE))
                    .PAYABLE_AMOUNT_UPTO_15_DAYS(rs.getString(PspclIfixQueryBuilder.PAYABLE_AMOUNT_UPTO_15_DAYS))
                    .TARIFF_TYPE(rs.getString(PspclIfixQueryBuilder.TARIFF_TYPE))
                    .ORDERBYCOLUMN(rs.getString(PspclIfixQueryBuilder.ORDERBYCOLUMN))
                    .billJsonData(billJsonData)
                    .auditDetails(auditDetails)
                    .build();


            pspclBillDetails.add(pspclBillDetail);
        }

        return pspclBillDetails;
    }
}
