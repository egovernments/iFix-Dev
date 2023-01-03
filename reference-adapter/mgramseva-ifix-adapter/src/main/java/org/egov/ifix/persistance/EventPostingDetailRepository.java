package org.egov.ifix.persistance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPostingDetailRepository  extends JpaRepository<EventPostingDetail, Long> {
	public List<EventPostingDetail> findByStatus(String status);

	 
}