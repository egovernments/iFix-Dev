package org.digit.program.utils;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Slf4j
public class CalculationUtil {
    private final SanctionRepository sanctionRepository;

    public CalculationUtil(SanctionRepository sanctionRepository) {
        this.sanctionRepository = sanctionRepository;
    }

    public void calculateAndUpdateSanctionAmount(String sanctionId, Double amount, Boolean isAddition) {
        log.info("calculateSanctionAmount");
        SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(sanctionId)).build();
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
        sanction.getAuditDetails().setLastModifiedBy("c");
        sanction.getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
        sanctionRepository.updateSanctionOnAllocation(sanction);
    }

}
