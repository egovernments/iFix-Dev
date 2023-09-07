package org.egov.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.egov.common.contract.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.Amount;
import org.egov.web.models.FiscalEvent;
import org.egov.web.models.FiscalEvent.EventTypeEnum;
import org.egov.web.models.ReceiverDTO;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.util.ObjectUtils;

@Component
public class FiscalEventRowMapper implements ResultSetExtractor<List<FiscalEvent>> {

    /**
     * Rowmapper that maps every column of the search result set to a key in the model.
     */

    @Autowired
    private ObjectMapper mapper;

    @Override
    public List<FiscalEvent> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<String, FiscalEvent> eventMap = new HashMap<>();
        while (rs.next()) {
            String currentId = rs.getString("id");
            FiscalEvent currentEvent = eventMap.get(currentId);
            if (!StringUtils.isBlank(currentId)) {


                if (currentEvent == null) {

                    AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
                            .createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
                            .lastModifiedTime(rs.getLong("lastmodifiedtime")).build();
                    currentEvent = FiscalEvent.builder()
                            .version(rs.getString("version"))
                            .id(rs.getString("id"))
                            .tenantId(rs.getString("tenantid"))
                            .sender(rs.getString("sender"))
                            .eventType(EventTypeEnum.valueOf(rs.getString("eventtype")))
                            .ingestionTime(rs.getLong("ingestiontime"))
                            .eventTime(rs.getLong("eventtime"))
                            .referenceId(rs.getString("referenceid"))
                            .linkedEventId(rs.getString("linkedeventid"))
                            .linkedReferenceId(rs.getString("linkedreferenceid"))
                            .attributes(getJsonValue((PGobject) rs.getObject("attributes")))
                            .auditDetails(auditDetails)
                            .build();

                }

                addAmountDetail(rs, currentEvent);
                addReceivers(rs, currentEvent);
                eventMap.put(currentId, currentEvent);
            }

        }
        return new ArrayList<>(eventMap.values());
    }

    private void addReceivers(ResultSet rs, FiscalEvent currentEvent) throws SQLException, DataAccessException {
        if(CollectionUtils.isEmpty(currentEvent.getReceivers())){
            List<String> receivers = new ArrayList<>();
            receivers.add(rs.getString("receiver"));
            currentEvent.setReceivers(receivers);
        }else{
            currentEvent.getReceivers().add(rs.getString("receiver"));
        }
    }

    private void addAmountDetail(ResultSet rs, FiscalEvent event) throws SQLException, DataAccessException {

        AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("amountcreatedby"))
                .createdTime(rs.getLong("amountcreatedtime")).lastModifiedBy(rs.getString("amountlastmodifiedby"))
                .lastModifiedTime(rs.getLong("amountlastmodifiedtime")).build();
        Amount amount = Amount.builder().id(rs.getString("amountid"))
                .amount(rs.getBigDecimal("amount"))
                //	.coaCode(rs.getString("coacode"))
                .coaId(rs.getString("coaid"))
                .fromBillingPeriod(rs.getLong("frombillingperiod"))
                .toBillingPeriod(rs.getLong("tobillingperiod"))
                .auditDetails(auditDetails).build();

        if (CollectionUtils.isEmpty(event.getAmountDetails())) {
            List<Amount> amounts = new ArrayList<>();
            amounts.add(amount);
            event.setAmountDetails(amounts);
        } else {
            event.getAmountDetails().add(amount);
        }


    }

    private JsonNode getJsonValue(PGobject pGobject) {
        try {
            if (Objects.isNull(pGobject) || Objects.isNull(pGobject.getValue()))
                return null;
            else
                return mapper.readTree(pGobject.getValue());
        } catch (IOException e) {
            throw new CustomException("SERVER_ERROR", "Exception occurred while parsing the additionalDetail json : " + e
                    .getMessage());
        }
    }

    private List<String> nodeToList(PGobject pGobject) {
        try {
            if (Objects.isNull(pGobject) || Objects.isNull(pGobject.getValue()))
                return null;
            else
                return mapper.readValue(pGobject.getValue(), new TypeReference<List<String>>() {
                });

        } catch (IOException e) {
            throw new CustomException("SERVER_ERROR", "Exception occurred while parsing the receiver json : " + e
                    .getMessage());
        }
    }

}
