package org.digit.program.utils;

import lombok.extern.slf4j.Slf4j;
import org.digit.program.constants.AllocationType;
import org.digit.program.constants.Status;
import org.digit.program.models.allocation.Allocation;
import org.digit.program.models.disburse.DisburseSearch;
import org.digit.program.models.disburse.Disbursement;
import org.digit.program.models.sanction.Sanction;
import org.digit.program.models.sanction.SanctionSearch;
import org.digit.program.repository.DisburseRepository;
import org.digit.program.repository.SanctionRepository;
import org.digit.program.service.EnrichmentService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CalculationUtil {
    private final SanctionRepository sanctionRepository;
    private final DisburseRepository disburseRepository;
    private final EnrichmentService enrichmentService;

    public CalculationUtil(SanctionRepository sanctionRepository, DisburseRepository disburseRepository, EnrichmentService enrichmentService) {
        this.sanctionRepository = sanctionRepository;
        this.disburseRepository = disburseRepository;
        this.enrichmentService = enrichmentService;
    }

    public Sanction calculateAndReturnSanctionForOnDisburseFailure(Disbursement disbursement, String senderId) {
        log.info("calculateSanctionAmount");
        SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(disbursement.getSanctionId())).build();
        Sanction sanction = sanctionRepository.searchSanction(sanctionSearch).get(0);
        sanction.setAllocatedAmount(sanction.getAllocatedAmount() + disbursement.getNetAmount());
        sanction.setAvailableAmount(sanction.getAvailableAmount() + disbursement.getNetAmount());
        enrichmentService.getAuditDetails(senderId, disbursement.getAuditDetails());
        return sanction;
    }

    public Sanction calculateAndReturnSanctionForDisburse(Disbursement disbursement, String senderId) {
        //Search disburse and return null if targetId is already present in db
        List<Disbursement> disbursements = disburseRepository.searchDisbursements(DisburseSearch.builder()
                .targetId(disbursement.getTargetId()).build());

        if (!disbursements.isEmpty()) {
            List<Status> statuses = disbursements.stream().
                    map(disbursementFromDB -> disbursementFromDB.getStatus().getStatusCode()).collect(Collectors.toList());
            if (statuses.contains(Status.PARTIAL)) {
                return null;
            }
        }
        log.info("calculateSanctionAmount");
        SanctionSearch sanctionSearch = SanctionSearch.builder().ids(Collections.singletonList(disbursement.getSanctionId())).build();
        Sanction sanction = sanctionRepository.searchSanction(sanctionSearch).get(0);
        Double allocatedAmount = sanction.getAllocatedAmount() - disbursement.getNetAmount();
        Double availableAmount = sanction.getAvailableAmount() - disbursement.getNetAmount();
        sanction.setAllocatedAmount(allocatedAmount);
        sanction.setAvailableAmount(availableAmount);
        sanction.setAuditDetails(enrichmentService.getAuditDetails(senderId, sanction.getAuditDetails()));
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
