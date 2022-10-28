package org.egov.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application_test.properties")
public class TestProperties {

    @Value("${test.data.package}")
    private String testDataPackage;

    @Value("${fetch.pspcl.bill.request.data}")
    private String pspclBillRequest;

    @Value("${fetch.pspcl.payment.request.data}")
    private String pspclPaymentRequest;

    @Value("${fetch.pspcl.bill.response.data}")
    private String pspclBillResponse;

    @Value("${fetch.pspcl.payment.response.data}")
    private String pspclPaymentResponse;

}
