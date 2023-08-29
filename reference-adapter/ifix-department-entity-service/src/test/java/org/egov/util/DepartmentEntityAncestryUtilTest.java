package org.egov.util;

import org.egov.config.TestDataFormatter;
import org.egov.web.models.DepartmentEntityDTO;
import org.egov.web.models.DepartmentEntityAncestry;
import org.egov.web.models.DepartmentEntityRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Disabled("TODO: Need to work on it")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class DepartmentEntityAncestryUtilTest {

    @InjectMocks
    private DepartmentEntityAncestryUtil departmentEntityAncestryUtil;

    @Autowired
    private TestDataFormatter testDataFormatter;

    private DepartmentEntityRequest departmentEntityRequest;


    @BeforeAll
    void init() throws IOException {
        departmentEntityRequest = testDataFormatter.getDeptEntityCreateRequestData();
    }

    @Test
    void testCreateDepartmentEntityAncestryFromDepartmentEntity() {
        DepartmentEntityAncestry actualCreateDepartmentEntityAncestryFromDepartmentEntityResult = this.departmentEntityAncestryUtil
                .createDepartmentEntityAncestryFromDepartmentEntity(new DepartmentEntityDTO());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getAuditDetails());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getTenantId());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getName());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getId());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getHierarchyLevel());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getDepartmentId());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getCode());
        assertTrue(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getChildren().isEmpty());
    }

    @Test
    void testCreateDepartmentEntityAncestryFromDepartmentEntityWithDepartmentEntity() {
        DepartmentEntityAncestry actualCreateDepartmentEntityAncestryFromDepartmentEntityResult = this.departmentEntityAncestryUtil
                .createDepartmentEntityAncestryFromDepartmentEntity(departmentEntityRequest.getDepartmentEntityDTO());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getAuditDetails());
        assertNotNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getTenantId());
        assertNotNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getName());
        assertNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getId());
        assertNotNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getHierarchyLevel().intValue());
        assertNotNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getDepartmentId());
        assertNotNull(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getCode());
        assertTrue(actualCreateDepartmentEntityAncestryFromDepartmentEntityResult.getChildren().isEmpty());
    }
}

