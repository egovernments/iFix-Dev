package org.egov.ifix.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PspclEventDetailRepository extends JpaRepository<PspclEventDetail, Long> {
    @Query(value = "SELECT eventId FROM PspclEventDetail WHERE createdDate >= :startDate AND createdDate <= :endDate " +
            "AND success = :isSuccess")
    List<String> findAllByCreatedDateRange(@Param("startDate")Date startDate, @Param("endDate") Date endDate,
                                           @Param("isSuccess") boolean isSuccess);

    @Query(value = "SELECT eventId FROM PspclEventDetail WHERE createdDate >= :startDate AND createdDate <= :endDate " +
            "AND success = :isSuccess AND eventType = :eventType")
    List<String> findAllByCreatedDateRangeAndEventType(@Param("startDate")Date startDate,
                                                       @Param("endDate") Date endDate,
                                                       @Param("isSuccess") boolean isSuccess,
                                                       @Param("eventType") String eventType);


}
