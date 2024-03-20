package org.digit.exchange.repository;

import org.digit.exchange.model.ExchangeServerSearch;
import org.digit.exchange.model.ExchangeServer;
import org.digit.exchange.repository.querybuilder.ExchangeServerQueryBuilder;
import org.digit.exchange.repository.rowmapper.ExchangeServerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ExchangeServerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ExchangeServerQueryBuilder serverQueryBuilder;
    private final ExchangeServerRowMapper serverRowMapper;

    public ExchangeServerRepository(JdbcTemplate jdbcTemplate, ExchangeServerQueryBuilder serverQueryBuilder, ExchangeServerRowMapper serverRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.serverQueryBuilder = serverQueryBuilder;
        this.serverRowMapper = serverRowMapper;
    }

    public List<ExchangeServer> searchServers(ExchangeServerSearch serverSearch){

        List<Object> preparedStatementValues = new ArrayList<>();
        String queryStr = serverQueryBuilder.buildExchangeServerSearchQuery(serverSearch, preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), serverRowMapper);
    }

}
