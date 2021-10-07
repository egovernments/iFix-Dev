package org.egov.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.web.models.*;
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
        }else {
            return new File(classLoader.getResource(fileName).getFile());
        }
    }


    /**
     * @return
     */
    public GovernmentRequest getGovernmentRequestData() throws IOException {
        GovernmentRequest governmentRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateRequestData()),
                        GovernmentRequest.class);

        return governmentRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public GovernmentRequest getHeadlessGovernmentRequestData() throws IOException {
        GovernmentRequest governmentRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateRequestDataHeadless()),
                        GovernmentRequest.class);

        return governmentRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public GovernmentResponse getGovernmentCreateResponseData() throws IOException {
        GovernmentResponse governmentResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateResponseData()),
                        GovernmentResponse.class);

        return governmentResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public GovernmentSearchRequest getGovernmentSearchRequestData() throws IOException {
        GovernmentSearchRequest governmentSearchRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getGovSearchRequestData()),
                        GovernmentSearchRequest.class);

        return governmentSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public GovernmentResponse getGovernmentSearchResponseData() throws IOException {
        GovernmentResponse governmentResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getGovSearchResponseData()),
                        GovernmentResponse.class);

        return governmentResponse;
    }


    public COARequest getCoaRequestData() throws IOException {
        COARequest coaRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateRequestData()),
                        COARequest.class);

        return coaRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public COARequest getHeadlessCoaRequestData() throws IOException {
        COARequest coaRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateRequestDataHeadless()),
                        COARequest.class);

        return coaRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public COAResponse getCoaCreateResponseData() throws IOException {
        COAResponse coaResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateResponseData()),
                        COAResponse.class);

        return coaResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public COASearchRequest getCoaSearchRequestData() throws IOException {
        COASearchRequest coaSearchRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getCoaSearchRequestData()),
                        COASearchRequest.class);

        return coaSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public COAResponse getCoaSearchResponseData() throws IOException {
        COAResponse coaResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getCoaSearchResponseData()),
                        COAResponse.class);

        return coaResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public ProjectSearchRequest getProjectSearchRequestData() throws IOException {
        ProjectSearchRequest projectSearchRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getProjectSearchRequestData()),
                        ProjectSearchRequest.class);

        return projectSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public ProjectResponse getProjectSearchReponseData() throws IOException {
        ProjectResponse projectResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getProjectSearchResponseData()),
                        ProjectResponse.class);

        return projectResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public ProjectRequest getProjectCreateRequestData() throws IOException {
        ProjectRequest projectRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getProjectCreateRequestData()),
                        ProjectRequest.class);

        return projectRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentRequest getDepartmentCreateRequestData() throws IOException {
        DepartmentRequest departmentRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getDepartmentCreateRequestData()),
                        DepartmentRequest.class);

        return departmentRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentSearchRequest getDepartmentSearchRequestData() throws IOException {
        DepartmentSearchRequest departmentSearchRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getDepartmentSearchRequestData()),
                        DepartmentSearchRequest.class);

        return departmentSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentResponse getDepartmentSearchResponseData() throws IOException {
        DepartmentResponse departmentResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getDepartmentSearchResponseData()),
                        DepartmentResponse.class);

        return departmentResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public ExpenditureSearchRequest getExpenditureSearchRequestData() throws IOException {
        ExpenditureSearchRequest expenditureSearchRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getExpenditureSearchRequestData()),
                        ExpenditureSearchRequest.class);

        return expenditureSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public ExpenditureResponse getExpenditureSearchResponseData() throws IOException {
        ExpenditureResponse expenditureResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getExpenditureSearchResponseData()),
                        ExpenditureResponse.class);

        return expenditureResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public ExpenditureRequest getExpenditureCreateRequestData() throws IOException {
        ExpenditureRequest expenditureRequest = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getExpenditureCreateRequestData()),
                        ExpenditureRequest.class);

        return expenditureRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public ExpenditureResponse getExpenditureCreateResponseData() throws IOException {
        ExpenditureResponse expenditureResponse = new ObjectMapper()
                .readValue(getFileFromClassLoaderResource(testProperties.getExpenditureCreateResponseData()),
                        ExpenditureResponse.class);

        return expenditureResponse;
    }


}
