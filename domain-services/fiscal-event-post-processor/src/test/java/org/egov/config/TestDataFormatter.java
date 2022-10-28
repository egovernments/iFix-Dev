package org.egov.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.models.FiscalEventDeReferenced;
import org.egov.models.FiscalEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TestDataFormatter {
    @Autowired
    private TestProperties testProperties;

    private ObjectMapper objectMapper;

    public TestDataFormatter() {
        objectMapper = new ObjectMapper();
    }

    /**
     * @param fileName
     * @return
     */
    private File getFileFromClassLoaderResource(String fileName) {
        String basePackage = testProperties.getTestDataPackage();
        ClassLoader classLoader = this.getClass().getClassLoader();

        if (basePackage != null && !basePackage.isEmpty()) {
            String baseURL = classLoader.getResource(basePackage).getFile();
            fileName = baseURL + File.separator + fileName;

            return new File(fileName);
        } else {
            return new File(classLoader.getResource(fileName).getFile());
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventRequest getFiscalEventValidatedData() throws IOException {
        FiscalEventRequest fiscalEventRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventValidatedData()),
                        FiscalEventRequest.class);

        return fiscalEventRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventDeReferenced getFiscalEventDereferencedData() throws IOException {
        FiscalEventDeReferenced fiscalEventDeReferenced = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventDereferencedData()),
                        FiscalEventDeReferenced.class);

        return fiscalEventDeReferenced;
    }


    public JsonNode getValidGovernmentSearchResponse() throws IOException {
        JsonNode validGovernmentResponse =
                new ObjectMapper().readTree(getFileFromClassLoaderResource(testProperties.getGovSearchResponseData()));
        return validGovernmentResponse;
    }

    public JsonNode getEmptyGovernmentSearchResponse() throws IOException {
        ObjectNode invalidGovernmentResponse =
                (ObjectNode) new ObjectMapper().readTree(getFileFromClassLoaderResource(testProperties.getGovSearchResponseData()));

        ArrayNode emptyArray = new ObjectMapper().createArrayNode();
        invalidGovernmentResponse.set("government", emptyArray);

        return invalidGovernmentResponse;
    }

    public JsonNode getCOASearchResponse() throws IOException {
        JsonNode coaSearchResponse =
                new ObjectMapper().readTree(getFileFromClassLoaderResource(testProperties.getCoaSearchResponseData()));
        return coaSearchResponse;
    }

}
