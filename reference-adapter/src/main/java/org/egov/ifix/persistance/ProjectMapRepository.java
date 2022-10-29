package org.egov.ifix.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMapRepository  extends JpaRepository<ProjectMap, Long> {
	public List<ProjectMap> findByClientProjectCode(String clientProjectCode);
	public List<ProjectMap> findByClientProjectCodeAndTenantId(String clientProjectCode,String tenantId);


	 
}