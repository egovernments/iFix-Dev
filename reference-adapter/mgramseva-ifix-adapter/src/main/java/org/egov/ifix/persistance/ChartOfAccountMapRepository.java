package org.egov.ifix.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChartOfAccountMapRepository extends JpaRepository<ChartOfAccountMap, Long> {
    ChartOfAccountMap findByClientCode(String clientCode);

    List<ChartOfAccountMap> findByClientCodeAndTenantId(String clientCode, String tenantId);


}