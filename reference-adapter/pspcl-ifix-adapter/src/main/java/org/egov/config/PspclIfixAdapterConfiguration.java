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
public class PspclIfixAdapterConfiguration {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Value("${pspcl.fetch.bill.url}")
    private String urlForFetchPspclBill;

    @Value("${pspcl.fetch.payment.url}")
    private String urlForFetchPspclPayment;

    @Value("${ifix.host}")
    private String ifixHost;

    @Value("${ifix.event.endpoint}")
    private String ifixEventUrl;

    @Value("${fiscal.event.tenantId}")
    private String tenantId;

    @Value("${demand.coaCode}")
    private String demandCoaCode;

    @Value("${receipt.coaCode}")
    private String receiptCoaCode;

    @Value("${fiscal.event.receiver}")
    private String fiscalEventReceiver;

    //keycloak
    @Value("${keycloak.credentials.clientid}")
    private String clientId;

    @Value("${keycloak.credentials.clientsecret}")
    private String clientSecret;

    @Value("${keycloak.credentials.granttype}")
    private String grantType;

    @Value("${keycloak.host}")
    private String keyCloakHost;

    @Value("${keycloak.token.url}")
    private String keyCloakAuthApi;

    //egov-mdms
    @Value("${egov.mdms.host}")
    private String egovMdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String egovMdmsSearchUrl;

}

