package org.egov.repository.rowmapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.egov.common.contract.AuditDetails;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.ChartOfAccount;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class COARowMapper  implements ResultSetExtractor<List<ChartOfAccount>> {

	/**
	 * Rowmapper that maps every column of the search result set to a key in the model.
	 */
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public List<ChartOfAccount> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, ChartOfAccount> coaMap = new HashMap<>();
		while (rs.next()) {
			String currentId = rs.getString("id");
			ChartOfAccount currentCOA = coaMap.get(currentId);
			if (null == currentCOA) {
				AuditDetails auditDetails = AuditDetails.builder().createdBy(rs.getString("createdby"))
						.createdTime(rs.getLong("createdtime")).lastModifiedBy(rs.getString("lastmodifiedby"))
						.lastModifiedTime(rs.getLong("lastmodifiedtime")).build();

				currentCOA = ChartOfAccount.builder().id(rs.getString("id"))
						.tenantId(rs.getString("tenantid"))
						.coaCode(rs.getString("coacode"))
						.majorHead(rs.getString("majorhead"))
						.majorHeadName(rs.getString("majorheadname"))
						.subMajorHead(rs.getString("submajorhead"))
						.subMajorHeadName(rs.getString("submajorheadname"))
						.minorHead(rs.getString("minorhead"))
						.minorHeadName(rs.getString("minorheadname"))
						.subHead(rs.getString("subhead"))
						.subHeadName(rs.getString("subheadname"))
						.groupHead(rs.getString("grouphead"))
						.groupHeadName(rs.getString("groupheadname"))
						.objectHead(rs.getString("objecthead"))
						.objectHeadName(rs.getString("objectheadname"))
						.attributes(getJsonValue((PGobject) rs.getObject("attributes")))
						.auditDetails(auditDetails).build();

				coaMap.put(currentId, currentCOA);
			}

		}
		return new ArrayList<>(coaMap.values());

	}
	
	private JsonNode getJsonValue(PGobject pGobject){
		try {
			if(Objects.isNull(pGobject) || Objects.isNull(pGobject.getValue()))
				return null;
			else
				return mapper.readTree( pGobject.getValue());
		} catch (IOException e) {
			throw new CustomException("SERVER_ERROR","Exception occurred while parsing the additionalDetail json : "+ e
					.getMessage());
		}
	}

}
