package org.egov.ifixmigrationtoolkit.repository;

import org.egov.ifixmigrationtoolkit.models.MigrationCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MigrationRepository {

    private static final String RESUME_FROM_QUERY = "SELECT COALESCE( (SELECT pagenumber FROM ifix_fiscal_events_migration WHERE tenantid = {TENANTID_PLACEHOLDER} ORDER BY createdtime DESC LIMIT 1\n" +
            ") , 0)";

    private static final String TOTAL_RECORDS_QUERY = "SELECT COALESCE( (SELECT totalnumberofrecordsmigrated FROM ifix_fiscal_events_migration WHERE tenantid = {TENANTID_PLACEHOLDER} ORDER BY createdtime DESC LIMIT 1\n" +
            ") , 0)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer getPageNumberToResumeFrom(String tenantId){
        Integer resumeFrom = jdbcTemplate.queryForObject(RESUME_FROM_QUERY.replace("{TENANTID_PLACEHOLDER}", "'" + tenantId + "'"), Integer.class);
        return resumeFrom;
    }

    public Long getTotalNumberOfRecordsMigrated(String tenantId){
        Long numberOfRecordsMigrated = jdbcTemplate.queryForObject(TOTAL_RECORDS_QUERY.replace("{TENANTID_PLACEHOLDER}", "'" + tenantId + "'"), Long.class);
        return numberOfRecordsMigrated;
    }

}
