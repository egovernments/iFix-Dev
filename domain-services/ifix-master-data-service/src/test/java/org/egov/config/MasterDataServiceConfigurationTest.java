package org.egov.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MasterDataServiceConfigurationTest {
    @Test
    void testInitialize() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration("UTC",
                "pb");
        masterDataServiceConfiguration.initialize();
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
        assertEquals("pb", masterDataServiceConfiguration.getRootLevelTenantId());
    }

    @Test
    void testInitialize2() {
        MasterDataServiceConfiguration masterDataServiceConfiguration = new MasterDataServiceConfiguration();
        masterDataServiceConfiguration.setTimeZone("UTC");
        masterDataServiceConfiguration.initialize();
        assertEquals("UTC", masterDataServiceConfiguration.getTimeZone());
    }
}

