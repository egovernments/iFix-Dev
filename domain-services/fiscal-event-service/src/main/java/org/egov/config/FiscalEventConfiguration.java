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

    @Value("${fiscal.kafka.es.push.topic}")
    private String fiscalEventESSinkTopic;

    @Value("${fiscal.event.kafka.postgres.topic}")
    private String fiscalEventPushToPostgresSink;

    @Value("${ifix.master.coa.host}")
    private String ifixMasterCoaHost;

    @Value("${ifix.master.coa.context.path}")
    private String ifixMasterCoaContextPath;

    @Value("${ifix.master.coa.search.path}")
    private String ifixMasterCoaSearchPath;

    @Value("${ifix.master.government.host}")
    private String ifixMasterGovernmentHost;

    @Value("${ifix.master.government.context.path}")
    private String ifixMasterGovernmentContextPath;

    @Value("${ifix.master.government.search.path}")
    private String ifixMasterGovernmentSearchPath;

    @Value("${root.level.tenant.id}")
    private String rootLevelTenantId;

    @Value("${fiscal.event.push.request.max.size}")
    private Integer fiscalEventPushReqMaxSize;

    @Value("${fiscal.event.default.offset}")
    private Long defaultOffset;

    @Value("${fiscal.event.default.limit}")
    private Long defaultLimit;

}
