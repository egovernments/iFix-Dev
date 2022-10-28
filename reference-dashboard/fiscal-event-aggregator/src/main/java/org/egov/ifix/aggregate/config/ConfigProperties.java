package org.egov.ifix.aggregate.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigProperties {

    @Value("${app.timezone}")
    private String timeZone;

    @Value("${druid.host}")
    private String druidHost;

    @Value("${druid.endPoint}")
    private String druidEndPoint;

    @Value("${fiscal.event.datasource}")
    private String fiscalEventDataSource;

    @Value("${druid.connect.protocol}")
    private String druidConnectProtocol;

    @Value("${druid.connect.port}")
    private String druidConnectPort;

}
