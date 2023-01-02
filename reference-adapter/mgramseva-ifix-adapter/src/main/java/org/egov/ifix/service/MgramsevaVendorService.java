package org.egov.ifix.service;

import javax.validation.constraints.NotNull;

public interface MgramsevaVendorService {

    String getVendorIdByTenantId(@NotNull String tenantId, @NotNull String name);
}
