package org.digit.program.repository;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.program.Program;
import org.digit.program.models.program.ProgramSearch;
import org.digit.program.repository.querybuilder.ExchangeCodeQueryBuilder;
import org.digit.program.repository.querybuilder.ProgramQueryBuilder;
import org.digit.program.repository.rowmapper.ProgramRowMapper;
import org.digit.program.utils.CommonUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ProgramRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProgramRowMapper programRowMapper;
    private final ProgramQueryBuilder programQueryBuilder;
    private final ExchangeCodeQueryBuilder exchangeCodeQueryBuilder;
    private final CommonUtil commonUtil;

    public ProgramRepository(JdbcTemplate jdbcTemplate, ProgramRowMapper programRowMapper, ProgramQueryBuilder programQueryBuilder, ExchangeCodeQueryBuilder exchangeCodeQueryBuilder, CommonUtil commonUtil) {
        this.jdbcTemplate = jdbcTemplate;
        this.programRowMapper = programRowMapper;
        this.programQueryBuilder = programQueryBuilder;
        this.exchangeCodeQueryBuilder = exchangeCodeQueryBuilder;
        this.commonUtil = commonUtil;
    }

    @Transactional
    public void saveProgram(Program program) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeInsertQuery = exchangeCodeQueryBuilder.buildExchangeCodeProgramInsertQuery(program, preparedStmtList);
        jdbcTemplate.update(exchangeCodeInsertQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String programInsertQuery = programQueryBuilder.buildProgramInsertQuery(program, preparedStmtList);
        jdbcTemplate.update(programInsertQuery, preparedStmtList.toArray());
        log.info("create Program persisted");
    }

    @Transactional
    public void updateProgram(Program program, Boolean isOnProgramCreate) {
        List<Object> preparedStmtList = new ArrayList<>();
        String exchangeCodeUpdateQuery = exchangeCodeQueryBuilder.buildExchangeCodeProgramUpdateQuery(program, preparedStmtList, isOnProgramCreate);
        jdbcTemplate.update(exchangeCodeUpdateQuery, preparedStmtList.toArray());

        preparedStmtList = new ArrayList<>();
        String programUpdateQuery = programQueryBuilder.buildProgramUpdateQuery(program, preparedStmtList, isOnProgramCreate);
        jdbcTemplate.update(programUpdateQuery, preparedStmtList.toArray());
        log.info("update Program persisted");
    }

    public List<Program> searchProgram(ProgramSearch programSearch) {
        List<Object> preparedStmtList = new ArrayList<>();
        programSearch.setPagination(commonUtil.enrichSearch(programSearch.getPagination()));
        String programSearchQuery = programQueryBuilder.buildProgramSearchQuery(programSearch, preparedStmtList);
        return jdbcTemplate.query(programSearchQuery, preparedStmtList.toArray(), programRowMapper);
    }

}
