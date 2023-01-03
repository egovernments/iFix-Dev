package org.egov.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MasterDataServiceConfigurationTest {
    @Test
    void testInitialize() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration("UTC",
                "localhost",
                "Ifix Master Expenditure Context Path", "Ifix Master Expenditure Search Path", "localhost",
                "Department Entity Context Path", "Department Entity Search Path", "localhost",
                "Ifix Master Department Context Path", "Ifix Master Department Search Path");
        masterDataServiceConfiguration.initialize();
        assertEquals("Department Entity Context Path", masterDataServiceConfiguration.getDepartmentEntityContextPath());
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
//        assertEquals("Ifix Master Government Search Path",
//                masterDataServiceConfiguration.getIfixMasterGovernmentSearchPath());
//        assertEquals("localhost", masterDataServiceConfiguration.getIfixMasterGovernmentHost());
//        assertEquals("Ifix Master Government Context Path",
//                masterDataServiceConfiguration.getIfixMasterGovernmentContextPath());
        assertEquals("Ifix Master Expenditure Search Path",
                masterDataServiceConfiguration.getIfixMasterExpenditureSearchPath());
        assertEquals("localhost", masterDataServiceConfiguration.getIfixMasterExpenditureHost());
        assertEquals("Ifix Master Expenditure Context Path",
                masterDataServiceConfiguration.getIfixMasterExpenditureContextPath());
        assertEquals("localhost", masterDataServiceConfiguration.getIfixMasterDepartmentHost());
        assertEquals("Ifix Master Department Search Path",
                masterDataServiceConfiguration.getIfixMasterDepartmentSearchPath());
        assertEquals("Ifix Master Department Context Path",
                masterDataServiceConfiguration.getIfixMasterDepartmentContextPath());
        assertEquals("Department Entity Search Path", masterDataServiceConfiguration.getDepartmentEntitySearchPath());
        assertEquals("localhost", masterDataServiceConfiguration.getDepartmentEntityHost());
    }

    @Test
    void testInitialize2() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration();
        masterDataServiceConfiguration.setTimeZone("UTC");
        masterDataServiceConfiguration.initialize();
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
    }
}

