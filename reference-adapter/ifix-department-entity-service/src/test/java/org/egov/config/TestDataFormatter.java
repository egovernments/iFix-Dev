package org.egov.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.common.contract.response.ResponseHeader;
import org.egov.web.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

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
    public DepartmentEntityRequest getDeptEntityCreateRequestData() throws IOException {
        DepartmentEntityRequest departmentEntityRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptEntityCreateRequest()),
                        DepartmentEntityRequest.class);

        return departmentEntityRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentEntityRequest getDeptEntityUpdateRequestData() throws IOException {
        DepartmentEntityRequest departmentEntityRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptEntityUpdateRequest()),
                        DepartmentEntityRequest.class);

        return departmentEntityRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentEntityResponse getDeptEntityCreateResponseData() throws IOException {
        File fileContent = getFileFromClassLoaderResource(testProperties.getDeptEntityCreateResponse());
        JsonNode rootNode = objectMapper.readTree(fileContent);
        JsonNode headerNode = rootNode.get("responseHeader");
        JsonNode deptEntityNode = rootNode.get("departmentEntity");

        return (DepartmentEntityResponse.builder().responseHeader(objectMapper.convertValue(headerNode, ResponseHeader.class))
                .departmentEntity(Collections.singletonList(objectMapper.convertValue(deptEntityNode.get(0), DepartmentEntity.class)))
                .build());
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentEntitySearchRequest getDeptEntitySearchRequestData() throws IOException {
        DepartmentEntitySearchRequest departmentEntitySearchRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptEntitySearchRequest()),
                        DepartmentEntitySearchRequest.class);

        return departmentEntitySearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentEntityResponse getDeptEntitySearchResponseData() throws IOException {
        File fileContent = getFileFromClassLoaderResource(testProperties.getDeptEntitySearchResponse());
        JsonNode rootNode = objectMapper.readTree(fileContent);
        JsonNode headerNode = rootNode.get("responseHeader");
        JsonNode deptEntityNode = rootNode.get("departmentEntity");

        return (DepartmentEntityResponse.builder().responseHeader(objectMapper.convertValue(headerNode, ResponseHeader.class))
                .departmentEntity(Collections.singletonList(objectMapper.convertValue(deptEntityNode.get(0), DepartmentEntity.class)))
                .build());
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentHierarchyLevelRequest getDeptHierarchyCreateRequestData() throws IOException {
        DepartmentHierarchyLevelRequest departmentHierarchyLevelRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptHierarchyCreateRequest()),
                        DepartmentHierarchyLevelRequest.class);

        return departmentHierarchyLevelRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentHierarchyLevelResponse getDeptHierarchyCreateResponseData() throws IOException {
        DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptHierarchyCreateResponse()),
                        DepartmentHierarchyLevelResponse.class);

        return departmentHierarchyLevelResponse;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentHierarchyLevelSearchRequest getDeptHierarchySearchRequestData() throws IOException {
        DepartmentHierarchyLevelSearchRequest hierarchyLevelSearchRequest = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptHierarchySearchRequest()),
                        DepartmentHierarchyLevelSearchRequest.class);

        return hierarchyLevelSearchRequest;
    }

    /**
     * @return
     * @throws IOException
     */
    public DepartmentHierarchyLevelResponse getDeptHierarchySearchResponseData() throws IOException {
        DepartmentHierarchyLevelResponse departmentHierarchyLevelResponse = objectMapper
                .readValue(getFileFromClassLoaderResource(testProperties.getDeptHierarchySearchResponse()),
                        DepartmentHierarchyLevelResponse.class);

        return departmentHierarchyLevelResponse;
    }

    public JsonNode getProjectSearchResponse() throws IOException {
        JsonNode projectSearchResponse =
                new ObjectMapper().readTree(getFileFromClassLoaderResource(testProperties.getProjectSearchResponseData()));
        return projectSearchResponse;
    }

    public JsonNode getDepartmentSearchResponse() throws IOException {
        JsonNode departmentSearchResponse =
                new ObjectMapper().readTree(getFileFromClassLoaderResource(testProperties.getDepartmentSearchResponseData()));
        return departmentSearchResponse;
    }

}
