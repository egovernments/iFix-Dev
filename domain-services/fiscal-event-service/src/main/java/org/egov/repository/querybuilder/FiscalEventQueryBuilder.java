package org.egov.repository.querybuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.Criteria;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FiscalEventQueryBuilder {

    public String buildSearchQuery(Criteria searchCriteria, List<Object> preparedStmtList) {

        StringBuilder query = new StringBuilder("SELECT fiscal_event.*, " + "amount.id as amountid, "
                + "amount.fiscaleventid, " + "amount.coaid, " + "amount.frombillingperiod, "
                + "amount.tobillingperiod, " + "amount.amount, " + "amount.coaid, " + "amount.attributes, "
                + "amount.createdtime as amountcreatedtime, " + "amount.createdby as amountcreatedby, "
                + "amount.lastmodifiedtime as amountlastmodifiedtime, "
                + "amount.lastmodifiedby as amountlastmodifiedby " + "FROM eg_ifix_fiscal_event as fiscal_event "
                + "INNER JOIN eg_ifix_amount_detail as amount ON "
                + "fiscal_event.id=amount.fiscaleventid WHERE fiscal_event.tenantid = ?");

        preparedStmtList.add(searchCriteria.getTenantId());

        if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
            query.append(" AND fiscal_event.id IN ( ");
            setValuesForList(query, preparedStmtList, searchCriteria.getIds());
            query.append(")");
        }
        if (StringUtils.isNotBlank(searchCriteria.getEventType())) {
            query.append(" AND fiscal_event.eventType = ?");
            preparedStmtList.add(searchCriteria.getEventType());
        }
        if (searchCriteria.getFromEventTime() != null) {
            query.append(" AND fiscal_event.fromEventTime >= ?");
            preparedStmtList.add(searchCriteria.getFromEventTime());
        }
        if (searchCriteria.getToEventTime() != null) {
            query.append(" AND fiscal_event.toEventTime <= ?");
            preparedStmtList.add(searchCriteria.getToEventTime());
        }
        if (searchCriteria.getReferenceId() != null && !searchCriteria.getReferenceId().isEmpty()) {
            query.append(" AND fiscal_event.referenceid IN ( ");
            setValuesForList(query, preparedStmtList, searchCriteria.getReferenceId());
            query.append(")");
        }
        if (StringUtils.isNotBlank(searchCriteria.getReceiver())) {
            query.append(" AND (fiscal_event.receivers)::jsonb @> " + "'\"" + searchCriteria.getReceiver() + "\"'");
//			preparedStmtList.add("'"+searchCriteria.getReceiver()+"'");

        }
        if (searchCriteria.getFromIngestionTime() != null) {
            query.append(" AND fiscal_event.fromIngestionTime >= ?");
            preparedStmtList.add(searchCriteria.getFromIngestionTime());
        }
        if (searchCriteria.getToIngestionTime() != null) {
            query.append(" AND fiscal_event.toIngestionTime <= ?");
            preparedStmtList.add(searchCriteria.getToIngestionTime());
        }


        return query.toString();
    }

    /**
     * Sets prepared statement for values for a list
     *
     * @param query
     * @param preparedStmtList
     * @param ids
     */
    private void setValuesForList(StringBuilder query, List<Object> preparedStmtList, List<String> ids) {
        int len = ids.size();
        for (int i = 0; i < ids.size(); i++) {
            query.append("?");
            if (i != len - 1)
                query.append(", ");
            preparedStmtList.add(ids.get(i));
        }
    }

}
