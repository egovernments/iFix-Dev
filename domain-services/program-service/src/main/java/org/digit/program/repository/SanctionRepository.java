package org.digit.program.repository;

import org.digit.program.models.ExchangeCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanctionRepository extends ExchangeCodeRepository {

    @Query("SELECT ec FROM ExchangeCode ec " +
            "WHERE (:ids is null or ec.id in :ids) " +
            "AND (:parentId is null or ec.parentId = :parentId) " +
            "AND (:name is null or ec.name = :name) " +
            "AND (:programCode is null or ec.programCode = :programCode) " +
            "AND (:locationCode is null or ec.locationCode = :locationCode)")
    List<ExchangeCode> findByCriteria(List<String> ids, String programCode, String locationCode, Sort sort);

}
