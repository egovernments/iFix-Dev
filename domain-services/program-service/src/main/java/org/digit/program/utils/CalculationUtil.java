package org.digit.program.utils;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CalculationUtil {
    private final SanctionRepository sanctionRepository;
    private final DisburseRepository disburseRepository;

    public CalculationUtil(SanctionRepository sanctionRepository, DisburseRepository disburseRepository) {
        this.sanctionRepository = sanctionRepository;
        this.disburseRepository = disburseRepository;
    }

    public Sanction calculateAndReturnSanctionForDisburse(String sanctionId, Double amount, Boolean isAddition) {
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
        return sanction;
    }

    public List<Sanction> calculateAndReturnSanction(List<Allocation> allocations) {
        log.info("calculateSanctionAmount");
        Set<String> sanctionIds = allocations.stream().map(Allocation::getSanctionId).collect(Collectors.toSet());
        Map<String, Sanction> sanctionIdVsSanction = sanctionRepository.searchSanction(SanctionSearch.builder()
                .ids(new ArrayList<>(sanctionIds)).build()).stream().collect(Collectors.toMap(Sanction::getId, sanction -> sanction));
        for (Allocation allocation : allocations) {
            if (allocation.getType().equals(AllocationType.ALLOCATION)) {
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAllocatedAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAllocatedAmount() + allocation.getAmount());
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAvailableAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAvailableAmount() + allocation.getAmount());
            } else {
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAllocatedAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAllocatedAmount() - allocation.getAmount());
                sanctionIdVsSanction.get(allocation.getSanctionId()).setAvailableAmount(sanctionIdVsSanction.get(allocation.getSanctionId()).getAvailableAmount() - allocation.getAmount());
            }
        }

        return new ArrayList<>(sanctionIdVsSanction.values());
    }

}
