package org.digit.program.repository;

import org.digit.program.models.Program;
import org.digit.program.models.ProgramSearch;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.querybuilder.ProgramQueryBuilder;
import org.digit.program.repository.rowmapper.ProgramRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProgramRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProgramRowMapper programRowMapper;

    @Autowired
    private ProgramQueryBuilder programQueryBuilder;

    @Autowired
    private ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;

    @Transactional
    public void saveProgram(Program program) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeInsertQuery(program, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String programInsertQuery = programQueryBuilder.buildProgramInsertQuery(program, preparedStmtList);
        jdbcTemplate.update(programInsertQuery, preparedStmtList.toArray());
    }

    @Transactional
    public void updateProgram(Program program) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeUpdateQuery(program, preparedStmtList);
        jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String programUpdateQuery = programQueryBuilder.buildProgramUpdateQuery(program, preparedStmtList);
        jdbcTemplate.update(programUpdateQuery, preparedStmtList.toArray());
    }

    public List<Program> searchProgram(ProgramSearch programSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        String programSearchQuery = programQueryBuilder.buildProgramSearchQuery(programSearch, preparedStmtList);
        return jdbcTemplate.query(programSearchQuery, preparedStmtList.toArray(), programRowMapper);
    }

}
