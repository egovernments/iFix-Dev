package org.egov.repository.querybuilder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.web.models.COASearchCriteria;
import

org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChartOfAccountQueryBuilder {
	
	
	
	public String buildSearchQuery(COASearchCriteria searchCriteria, List<Object> preparedStmtList) {

		StringBuilder query = new StringBuilder("SELECT id, tenantid, coacode, majorhead, majorheadname, "
				+ "submajorhead, submajorheadname, minorhead, minorheadname, subhead, subheadname, "
				+ "grouphead, groupheadname, objecthead, objectheadname, attributes, "
				+ "createdtime, createdby, lastmodifiedtime, lastmodifiedby "
				+ "FROM eg_ifix_chartofaccount WHERE tenantid = ?");
		preparedStmtList.add(searchCriteria.getTenantId());

		if (searchCriteria.getIds() != null && !searchCriteria.getIds().isEmpty()) {
			query.append(" AND id IN ( ");
			setValuesForList(query, preparedStmtList, searchCriteria.getIds());
			query.append(")");
		}

		
		if (StringUtils.isNotBlank(searchCriteria.getMajorHead())) {
			query.append(" AND majorhead = ?");
			preparedStmtList.add(searchCriteria.getMajorHead());
		}
		if (StringUtils.isNotBlank(searchCriteria.getMinorHead())) {
			query.append(" AND minorhead = ?");
			preparedStmtList.add(searchCriteria.getMinorHead());
		}

		if (StringUtils.isNotBlank(searchCriteria.getGroupHead())) {
			query.append(" AND grouphead = ?");
			preparedStmtList.add(searchCriteria.getGroupHead());
		}

		if (StringUtils.isNotBlank(searchCriteria.getObjectHead())) {
			query.append(" AND objecthead = ?");
			preparedStmtList.add(searchCriteria.getObjectHead());
		
		}

		if (StringUtils.isNotBlank(searchCriteria.getSubHead())) {
			query.append(" AND subhead = ?");
			preparedStmtList.add(searchCriteria.getSubHead());
		}

		if (StringUtils.isNotBlank(searchCriteria.getSubMajorHead())) {
			query.append(" AND submajorhead = ?");
			preparedStmtList.add(searchCriteria.getSubMajorHead());
		}

		if (searchCriteria.getCoaCodes() != null && !searchCriteria.getCoaCodes().isEmpty()) {
			query.append(" AND coacode IN ( ");
			setValuesForList(query, preparedStmtList, searchCriteria.getCoaCodes());
			query.append(")");
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
