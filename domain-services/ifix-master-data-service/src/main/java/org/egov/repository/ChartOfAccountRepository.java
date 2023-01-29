package org.egov.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.producer.Producer;
import org.egov.repository.querybuilder.ChartOfAccountQueryBuilder;
import org.egov.repository.rowmapper.COARowMapper;
import org.egov.web.models.COASearchCriteria;
import org.egov.web.models.ChartOfAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository

@Slf4j
public class ChartOfAccountRepository {

	@Autowired
	private ChartOfAccountQueryBuilder coaQueryBuilder;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Producer producer;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ChartOfAccountQueryBuilder chartOfAccountQueryBuilder;
	
	@Autowired
	private COARowMapper coaRowMapper;

	public List<ChartOfAccount> search(COASearchCriteria searchCriteria) {
		List<ChartOfAccount> chartOfAccounts = new ArrayList<>();
		List<Object> preparedStmtList = new ArrayList<>();
		try {
			String coaSearchQuery = chartOfAccountQueryBuilder.buildSearchQuery(searchCriteria, preparedStmtList);
			chartOfAccounts = jdbcTemplate.query(coaSearchQuery, preparedStmtList.toArray(), coaRowMapper);
			if(CollectionUtils.isEmpty(chartOfAccounts))
				return new ArrayList<>();
		}catch(Exception e) {
			log.error("Exception while fetching from DB: " + e);
			return chartOfAccounts;
		}

		return chartOfAccounts;
	}
}
