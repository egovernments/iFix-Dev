//package org.egov.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.io.FileUtils;
//import org.egov.web.models.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//
//@Component
//public class TestDataFormatter {
//    @Autowired
//    private TestProperties testProperties;
//
//    /**
//     * @param fileName
//     * @return
//     */
//    private File getFileFromClassLoaderResource(String fileName) {
//        String basePackage = testProperties.getTestDataPackage();
//        ClassLoader classLoader = this.getClass().getClassLoader();
//
//        if (basePackage != null && !basePackage.isEmpty()) {
//            String baseURL = classLoader.getResource(basePackage).getFile();
//            fileName = baseURL + File.separator + fileName;
//
//            return new File(fileName);
//        } else {
//            return new File(classLoader.getResource(fileName).getFile());
//        }
//    }
//
//
//    /**
//     * @return
//     */
//    public GovernmentRequest getGovernmentRequestData() throws IOException {
//        GovernmentRequest governmentRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateRequestData()),
//                        GovernmentRequest.class);
//
//        return governmentRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public GovernmentRequest getHeadlessGovernmentRequestData() throws IOException {
//        GovernmentRequest governmentRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateRequestDataHeadless()),
//                        GovernmentRequest.class);
//
//        return governmentRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public GovernmentResponse getGovernmentCreateResponseData() throws IOException {
//        GovernmentResponse governmentResponse = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovCreateResponseData()),
//                        GovernmentResponse.class);
//
//        return governmentResponse;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public GovernmentSearchRequest getGovernmentSearchRequestData() throws IOException {
//        GovernmentSearchRequest governmentSearchRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovSearchRequestData()),
//                        GovernmentSearchRequest.class);
//
//        return governmentSearchRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public GovernmentResponse getGovernmentSearchResponseData() throws IOException {
//        GovernmentResponse governmentResponse = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovSearchResponseData()),
//                        GovernmentResponse.class);
//
//        return governmentResponse;
//    }
//
//
//    public COARequest getCoaRequestData() throws IOException {
//        COARequest coaRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateRequestData()),
//                        COARequest.class);
//
//        return coaRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public COARequest getHeadlessCoaRequestData() throws IOException {
//        COARequest coaRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateRequestDataHeadless()),
//                        COARequest.class);
//
//        return coaRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public COAResponse getCoaCreateResponseData() throws IOException {
//        COAResponse coaResponse = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getCoaCreateResponseData()),
//                        COAResponse.class);
//
//        return coaResponse;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public COASearchRequest getCoaSearchRequestData() throws IOException {
//        COASearchRequest coaSearchRequest = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getCoaSearchRequestData()),
//                        COASearchRequest.class);
//
//        return coaSearchRequest;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public COAResponse getCoaSearchResponseData() throws IOException {
//        COAResponse coaResponse = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getCoaSearchResponseData()),
//                        COAResponse.class);
//
//        return coaResponse;
//    }
//
//    /**
//     * @return
//     * @throws IOException
//     */
//    public Object getGovernmentSearchResponseDataAsObject() throws IOException {
//        Object governmentResponse = new ObjectMapper()
//                .readValue(getFileFromClassLoaderResource(testProperties.getGovSearchResponseData()),
//                        Object.class);
//
//        return governmentResponse;
//    }
//
//
//}
