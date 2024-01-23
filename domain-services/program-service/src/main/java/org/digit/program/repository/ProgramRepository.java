package org.digit.program.repository;

import org.digit.program.constants.SortOrder;
import org.digit.program.models.ExchangeCode;
import org.digit.program.models.Program;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends ExchangeCodeRepository {
//    List<ExchangeCode> findAllByIdIn(List<String> ids);
//    List<ExchangeCode> findByParentId(String parentId);
//    List<ExchangeCode> findByName(String name);
//    List<ExchangeCode> findByProgramCode(String programCode);
//    List<ExchangeCode> findByLocationCode(String locationCode);

    @Query("SELECT ec FROM ExchangeCode ec " +
            "WHERE (:ids is null or ec.id in :ids) " +
            "AND (:parentId is null or ec.parentId = :parentId) " +
            "AND (:name is null or ec.name = :name) " +
            "AND (:programCode is null or ec.programCode = :programCode) " +
            "AND (:locationCode is null or ec.locationCode = :locationCode)")
    List<ExchangeCode> findByCriteria(List<String> ids, String name, String parentId, String programCode, String locationCode, Sort sort);

//    Iterable<Program> findByType(String name);
//    Iterable<ExchangeCode> findByParentId(@Param("parentId") String parentId);

}
