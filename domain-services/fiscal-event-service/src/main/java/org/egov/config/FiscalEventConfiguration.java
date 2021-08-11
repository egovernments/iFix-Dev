package org.egov.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
public class FiscalEventConfiguration {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Value("${fiscal.kafka.push.topic}")
    private String fiscalPushRequest;

    @Value("${ifix.master.host}")
    private String ifixMasterHost;

    @Value("${ifix.master.context.path}")
    private String ifixMasterContextPath;

    @Value("${ifix.master.coa.search.path}")
    private String coaSearchPath;
}
