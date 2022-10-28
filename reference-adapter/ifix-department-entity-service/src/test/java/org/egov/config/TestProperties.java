package org.egov.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application_test.properties")
public class TestProperties {

    @Value("${test.data.package}")
    private String testDataPackage;

    @Value("${dept.entity.create.request.data}")
    private String deptEntityCreateRequest;

    @Value("${dept.entity.update.request.data}")
    private String deptEntityUpdateRequest;

    @Value("${dept.entity.create.response.data}")
    private String deptEntityCreateResponse;

    @Value("${dept.entity.search.request.data}")
    private String deptEntitySearchRequest;

    @Value("${dept.entity.search.response.data}")
    private String deptEntitySearchResponse;

    @Value("${dept.hierarchy.create.request.data}")
    private String deptHierarchyCreateRequest;

    @Value("${dept.hierarchy.create.response.data}")
    private String deptHierarchyCreateResponse;

    @Value("${dept.hierarchy.search.request.data}")
    private String deptHierarchySearchRequest;

    @Value("${dept.hierarchy.search.response.data}")
    private String deptHierarchySearchResponse;

    @Value("${project.search.response.data}")
    private String projectSearchResponseData;

    @Value("${department.search.response.data}")
    private String departmentSearchResponseData;

}
