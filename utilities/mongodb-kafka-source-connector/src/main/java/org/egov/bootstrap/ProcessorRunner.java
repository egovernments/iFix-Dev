package org.egov.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.egov.processor.DataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessorRunner implements ApplicationRunner {
    @Autowired
    DataProcessor dataProcessor;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isPublished = dataProcessor.processAndPublishDereferenceFiscalEvent();

        log.info(">>>>> Data publish status: " + isPublished);

    }
}
