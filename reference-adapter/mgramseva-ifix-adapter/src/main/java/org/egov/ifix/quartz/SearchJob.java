package org.egov.ifix.quartz;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.service.PspclEventService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class SearchJob extends QuartzJobBean {
    @Autowired
    private PspclEventService pspclEventService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(">>>>>>>>>>>> JOB started :: " + new Date(System.currentTimeMillis()) + " <<<<<<<<<<<<<<<<");

        try {
            pspclEventService.processPspclEventForMgramseva("Demand");
        } catch (Exception e) {
            log.error("Issues while processing demand", e);
        }

        try {
            pspclEventService.processPspclEventForMgramseva("Receipt");
        } catch (Exception e) {
            log.error("Issues while processing Receipt", e);
        }

        log.info(">>>>>>>>>>>> JOB completed :: " + new Date(System.currentTimeMillis()) + " <<<<<<<<<<<<<<<<");
    }
}
