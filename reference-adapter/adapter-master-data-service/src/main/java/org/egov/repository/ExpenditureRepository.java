package org.egov.repository;

import org.egov.repository.queryBuilder.ExpenditureQueryBuilder;
import org.egov.repository.rowmapper.ExpenditureRowMapper;
import org.egov.web.models.ExpenditureDTO;
import org.egov.web.models.ExpenditureSearchCriteria;
import org.egov.web.models.persist.Expenditure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpenditureRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ExpenditureQueryBuilder expenditureQueryBuilder;

    @Autowired
    private ExpenditureRowMapper expenditureRowMapper;

    /**
     * @param expenditureSearchCriteria
     * @return
     */
    public List<Expenditure> findAllByCriteria(ExpenditureSearchCriteria expenditureSearchCriteria) {
        return jdbcTemplate.query(
                expenditureQueryBuilder
                        .buildSearchQuery(expenditureSearchCriteria), expenditureRowMapper);
    }
}
