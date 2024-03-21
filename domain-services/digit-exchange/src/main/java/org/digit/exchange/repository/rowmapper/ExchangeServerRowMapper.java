package org.digit.exchange.repository.rowmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.digit.exchange.model.AuditDetails;
import org.digit.exchange.model.ExchangeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
public class ExchangeServerRowMapper implements ResultSetExtractor<List<ExchangeServer>> {


    private final ObjectMapper mapper;

    @Autowired
    public ExchangeServerRowMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public List<ExchangeServer> extractData(java.sql.ResultSet rs)  throws SQLException {

        Map<String, ExchangeServer> exchangeServerMap = new LinkedHashMap<>();

        while (rs.next()) {
            String id = rs.getString("id");
            ExchangeServer server = exchangeServerMap.get(id);

            if (server == null) {

                exchangeServerMap.clear();

                AuditDetails exchangeAuditDetails = AuditDetails.builder()
                        .lastModifiedTime(rs.getLong("last_modified_time"))
                        .createdTime((Long) rs.getObject("created_time"))
                        .lastModifiedBy(rs.getString("last_modified_by"))
                        .createdBy(rs.getString("created_by"))
                        .build();
                server = ExchangeServer.builder()
                        .id(id)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .hostUrl(rs.getString("host_url"))
                        .publicKey(rs.getString("public_key"))
                        .isActive(rs.getBoolean("is_active"))
                        .auditDetails(exchangeAuditDetails)

                        .build();

                exchangeServerMap.put(server.getId(), server);
            }
        }
        log.debug("converting map to list object ::: " + exchangeServerMap.values());
        return new ArrayList<>(exchangeServerMap.values());
    }

}
