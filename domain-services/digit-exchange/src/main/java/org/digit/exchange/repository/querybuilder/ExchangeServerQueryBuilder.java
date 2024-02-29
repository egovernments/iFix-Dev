package org.digit.exchange.repository.querybuilder;

import org.digit.exchange.model.ExchangeServerSearch;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class ExchangeServerQueryBuilder {

    public static final String EXCHANGE_SERVER_SEARCH_QUERY = "SELECT id, name, description, host_url, public_key, is_active, created_time, created_by, last_modified_time, last_modified_by FROM eg_digit_exchange_servers as server_details";

    public String buildExchangeServerSearchQuery(ExchangeServerSearch serverSearch, List<Object> preparedStmtList) {
        StringBuilder serverSearchQuery = new StringBuilder(EXCHANGE_SERVER_SEARCH_QUERY);

        List<String> ids = serverSearch.getIds();
        if (ids != null && !ids.isEmpty()) {
            addClauseIfRequired(serverSearchQuery, preparedStmtList);
            serverSearchQuery.append(" server_details.id IN (").append(createQuery(ids)).append(")");
            addToPreparedStatement(preparedStmtList, ids);
        }
        if (serverSearch.getName() != null) {
            addClauseIfRequired(serverSearchQuery, preparedStmtList);
            serverSearchQuery.append(" server_details.name = ? ");
            preparedStmtList.add(serverSearch.getName());
        }
        if (serverSearch.getHostUrl() != null) {
            addClauseIfRequired(serverSearchQuery, preparedStmtList);
            serverSearchQuery.append(" server_details.host_url = ? ");
            preparedStmtList.add(serverSearch.getHostUrl());
        }
        // If isActive is not exists in search query then it will be TRUE as default
        if (serverSearch.getIsActive() == null) {
            serverSearch.setIsActive(Boolean.TRUE);
        }
        addClauseIfRequired(serverSearchQuery, preparedStmtList);
        serverSearchQuery.append(" server_details.is_active = ? ");
        preparedStmtList.add(serverSearch.getIsActive());

        return serverSearchQuery.toString();
    }


    private void addToPreparedStatement(List<Object> preparedStmtList, Collection<String> ids) {
        preparedStmtList.addAll(ids);
    }

    private String createQuery(Collection<String> ids) {
        StringBuilder builder = new StringBuilder();
        int length = ids.size();
        for (int i = 0; i < length; i++) {
            builder.append(" ? ");
            if (i != length - 1) builder.append(",");
        }
        return builder.toString();
    }

    private void addClauseIfRequired(StringBuilder query, List<Object> preparedStmtList) {
        if (preparedStmtList.isEmpty()) {
            query.append(" WHERE ");
        } else {
            query.append(" AND ");
        }
    }

}
