package org.digit.program.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.digit.program.configuration.ProgramConfiguration;
import org.digit.program.constants.MessageType;
import org.digit.program.constants.SortOrder;
import org.digit.program.models.Pagination;
import org.digit.program.models.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.digit.program.constants.Error.PARSING_ERROR;
import static org.digit.program.constants.Error.PARSING_ERROR_MSG;

@Component
public class CommonUtil {

    private final ProgramConfiguration configs;
    private final ObjectMapper mapper;

    public CommonUtil(ProgramConfiguration configs, ObjectMapper mapper) {
        this.configs = configs;
        this.mapper = mapper;
    }

    public Pagination enrichSearch(Pagination pagination) {
        if (pagination == null)
            pagination = Pagination.builder().build();

        if (pagination.getLimit() == null) {
            pagination.setLimit(configs.getSearchDefaultLimit());
        } else if (pagination.getLimit() > configs.getSearchMaxLimit()) {
            pagination.setLimit(configs.getSearchMaxLimit());
        }
        if (pagination.getOffset() == null) {
            pagination.setOffset(0);
        }
        if (StringUtils.isEmpty(pagination.getSortBy())) {
            pagination.setSortBy("last_modified_time");
        }
        if (pagination.getSortOrder() == null) {
            pagination.setSortOrder(SortOrder.DESC);
        }
        return pagination;
    }

    public JsonNode getJsonNode(ResultSet rs, String key) throws SQLException {
        JsonNode jsonNode = null;

        try {

            PGobject obj = (PGobject) rs.getObject(key);
            if (obj != null) {
                jsonNode = mapper.readTree(obj.getValue());
            }

        } catch (IOException e) {
            throw new CustomException(PARSING_ERROR, PARSING_ERROR_MSG + e.getMessage());
        }

        if(jsonNode== null || jsonNode.isEmpty())
            jsonNode = null;

        return jsonNode;
    }

    public PGobject getPGObject(Object jsonObject) {

        String value = null;
        try {
            value = mapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new CustomException(PARSING_ERROR, PARSING_ERROR_MSG + e.getMessage());
        }

        PGobject json = new PGobject();
        json.setType("jsonb");
        try {
            json.setValue(value);
        } catch (SQLException e) {
            throw new CustomException(PARSING_ERROR, PARSING_ERROR_MSG + e.getMessage());
        }
        return json;
    }

    public void updateUri(RequestHeader requestHeader){
        if (!requestHeader.getMessageType().toString().contains("on-")) {
            requestHeader.setReceiverId(requestHeader.getSenderId());
            requestHeader.setSenderId(requestHeader.getSenderId().split("@")[0] + "@" + configs.getDomain());
            requestHeader.setMessageType(MessageType.fromValue("on-" + requestHeader.getMessageType().toString()));
        }
    }

    public boolean isSameDomain(String url1, String url2) {
        boolean isValid = false;
        String domain1 = extractHostUrlFromURL(url1);
        String domain2 = extractHostUrlFromURL(url2);
        if (domain1 != null && domain2 != null) {
            isValid = domain1.equals(domain2);
        }
        return isValid;
    }

    public String extractHostUrlFromURL(String input) {
        // Regular expression pattern to match the domain with http/https
        // Pattern pattern = Pattern.compile("(https?://[a-zA-Z0-9.-]+)");
        // Regex pattern to match the domain with http/https
        String regex = "\\bhttps?://\\S+\\b";
        // Regular expression pattern to match the domain with http/https
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(); // Returns the matched domain with http/https
        } else {
            return null; // No match found
        }
    }
}
