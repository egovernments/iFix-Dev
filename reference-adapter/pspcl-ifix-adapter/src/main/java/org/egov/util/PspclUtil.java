package org.egov.util;

import client.stub.GetBillResult;
import client.stub.GetPaymentResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.advice.LogExecutionTime;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.repository.SoapServiceRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.egov.util.PspclIfixAdapterConstant.*;

@Component
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class PspclUtil {

    @Autowired
    private PspclIfixAdapterConfiguration pspclIfixAdapterConfiguration;

    @Autowired
    private PspclIfixAdapterUtil pspclIfixAdapterUtil;

    @Autowired
    private SoapServiceRequestRepository soapServiceRequestRepository;

    @Autowired
    private XmlMapper xmlMapper;

    @Autowired
    private ObjectMapper objectMapper;


    @LogExecutionTime
    public List<GetBillResult> getBillsFromPspcl(String accountNumber) {
        try {
            String reqFetchPspclBill = pspclIfixAdapterUtil.getFileAsString(PATH_FETCH_PSPCL_BILL);
            if (StringUtils.isBlank(reqFetchPspclBill)) {
                return Collections.emptyList();
            }
            reqFetchPspclBill = reqFetchPspclBill.replace(PLACEHOLDER_ACCOUNT_NO, accountNumber);
            String outputString = soapServiceRequestRepository.fetchResult(reqFetchPspclBill,
                    pspclIfixAdapterConfiguration.getUrlForFetchPspclBill()).toString();

            if (StringUtils.isBlank(outputString)) {
                log.debug("There is no response data from PSPCL system for fetch Bills");
                return Collections.emptyList();
            }

            JsonNode root = xmlMapper.readTree(outputString.getBytes());
            String nodeArray = root.findValue(TAG_GET_BILLS_RESULT).asText();

            GetBillResult[] getBillResults = objectMapper.readValue(nodeArray, GetBillResult[].class);
            return Arrays.asList(getBillResults);
        } catch (Exception ex) {
            log.error("Exception occurred while getting bill details from PSPCL system", ex);
        }
        return Collections.emptyList();
    }

    @LogExecutionTime
    public List<GetPaymentResult> getPaymentsFromPspcl(String accountNumber) {
        try {
            String reqFetchPspclPayment = pspclIfixAdapterUtil.getFileAsString(PATH_FETCH_PSPCL_PAYMENT);
            if (StringUtils.isBlank(reqFetchPspclPayment)) {
                return Collections.emptyList();
            }
            reqFetchPspclPayment = reqFetchPspclPayment.replace(PLACEHOLDER_ACCOUNT_NO, accountNumber);

            String outputString = soapServiceRequestRepository.fetchResult(reqFetchPspclPayment,
                    pspclIfixAdapterConfiguration.getUrlForFetchPspclPayment()).toString();

            if (StringUtils.isBlank(outputString)) {
                log.debug("There is no response data from PSPCL system for fetch Payments");
                return Collections.emptyList();
            }

            JsonNode root = xmlMapper.readTree(outputString.getBytes());
            String nodeArray = root.findValue(TAG_GET_PAYMENTS_RESULT).asText();

            GetPaymentResult[] getPaymentResults = objectMapper.readValue(nodeArray, GetPaymentResult[].class);
            return Arrays.asList(getPaymentResults);
        } catch (Exception ex) {
            log.error("Exception occurred while getting payment details from PSPCL system", ex);
        }
        return Collections.emptyList();
    }

}

