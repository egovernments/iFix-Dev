package org.digit.exchange.repository;

import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.model.ExchangeServerSearch;
import org.digit.exchange.model.ExchangeServer;
import org.digit.exchange.repository.querybuilder.ExchangeServerQueryBuilder;
import org.digit.exchange.repository.rowmapper.ExchangeServerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
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
        System.out.println(queryStr);
        System.out.println(preparedStatementValues);
        return jdbcTemplate.query(queryStr, preparedStatementValues.toArray(), serverRowMapper);
    }

}
