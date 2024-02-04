package org.digit.program.utils;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.constants.SortOrder;
import org.digit.program.models.Allocation;
import org.digit.program.models.Pagination;
import org.digit.program.models.Sanction;
import org.digit.program.models.SanctionSearch;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class CalculationUtil {
    private SanctionRepository sanctionRepository;

    public CalculationUtil(SanctionRepository sanctionRepository) {
        this.sanctionRepository = sanctionRepository;
    }

    public Sanction calculateSanctionAmount(String sanctionId, Double amount, Boolean isAddition) {
        log.info("calculateSanctionAmount");
        SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(sanctionId))
                .pagination(Pagination.builder().sortBy("last_modified_time").sortOrder(SortOrder.DESC).limit(1).offset(0).build()).build();
        Sanction sanction = sanctionRepository.searchSanction(sanctionSearch).get(0);
        Double allocatedAmount;
        Double availableAmount;
        if (isAddition) {
            allocatedAmount = sanction.getAllocatedAmount() + amount;
            availableAmount = sanction.getAvailableAmount() + amount;
        } else {
            allocatedAmount = sanction.getAllocatedAmount() - amount;
            availableAmount = sanction.getAvailableAmount() - amount;
        }
        sanction.setAllocatedAmount(allocatedAmount);
        sanction.setAvailableAmount(availableAmount);
        sanction.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        return sanction;
    }

}
