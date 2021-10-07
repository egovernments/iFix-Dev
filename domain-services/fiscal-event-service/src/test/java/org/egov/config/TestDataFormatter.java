package org.egov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.web.models.FiscalEventGetRequest;
import org.egov.web.models.FiscalEventRequest;
import org.egov.web.models.FiscalEventResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class TestDataFormatter {
    @Autowired
    private TestProperties testProperties;

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
    public FiscalEventGetRequest getFiscalEventSearchRequestData() throws IOException {
        FiscalEventGetRequest fiscalEventGetRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventSearchRequest()),
                        FiscalEventGetRequest.class);

        return fiscalEventGetRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventResponse getFiscalEventSearchResponseData() throws IOException {
        FiscalEventResponse fiscalEventResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventSearchResponse()),
                        FiscalEventResponse.class);

        return fiscalEventResponse;
    }

    ///

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventRequest getFiscalEventPushRequestData() throws IOException {
        FiscalEventRequest fiscalEventRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventPushRequest()),
                        FiscalEventRequest.class);

        return fiscalEventRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventResponse getFiscalEventPushResponseData() throws IOException {
        FiscalEventResponse fiscalEventResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventPushResponse()),
                        FiscalEventResponse.class);

        return fiscalEventResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public FiscalEventRequest getFiscalEventHeadlessPushData() throws IOException {
        FiscalEventRequest fiscalEventRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getFiscalEventPushHeadlessRequest()),
                        FiscalEventRequest.class);

        return fiscalEventRequest;
    }
}
