package org.egov.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MasterDataServiceConfigurationTest {
    @Test
    void testInitialize() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration("UTC",
                "localhost", "Ifix Master Government Context Path", "Ifix Master Government Search Path");
        masterDataServiceConfiguration.initialize();
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
        assertEquals("Ifix Master Government Search Path",
                masterDataServiceConfiguration.getIfixMasterGovernmentSearchPath());
        assertEquals("localhost", masterDataServiceConfiguration.getIfixMasterGovernmentHost());
        assertEquals("Ifix Master Government Context Path",
                masterDataServiceConfiguration.getIfixMasterGovernmentContextPath());
    }

    @Test
    void testInitialize2() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration();
        masterDataServiceConfiguration.setTimeZone("UTC");
        masterDataServiceConfiguration.initialize();
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
    }
}

