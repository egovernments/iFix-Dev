package org.digit.program.repository;

import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DisburseRepository {

    private JdbcTemplate jdbcTemplate;
    private ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;

    public DisburseRepository(JdbcTemplate jdbcTemplate, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
    }




}
