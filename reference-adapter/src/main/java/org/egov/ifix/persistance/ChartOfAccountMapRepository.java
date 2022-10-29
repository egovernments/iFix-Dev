package org.egov.ifix.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChartOfAccountMapRepository  extends JpaRepository<ChartOfAccountMap, Long> {
	public List<ChartOfAccountMap> findByClientCode(String clientCode);
	public List<ChartOfAccountMap> findByClientCodeAndTenantId(String clientCode,String tenantId);


	 
}