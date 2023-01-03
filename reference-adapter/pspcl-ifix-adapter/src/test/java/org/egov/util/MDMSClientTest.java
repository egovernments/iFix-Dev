package org.egov.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.egov.mdms.model.MdmsResponse;
import org.egov.model.AccountNumberGpMappingVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MDMSClientTest {

    private MDMSClient mDMSClient;

    @Spy
    private ObjectMapper objectMapper;

    @Mock
    private PspclIfixAdapterConfiguration pspclIfixAdapterConfiguration;

    @Spy
    private RestTemplate restTemplate;

    private List<AccountNumberGpMappingVO> acnGpMappingVOs;

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        mDMSClient = new MDMSClient(pspclIfixAdapterConfiguration, restTemplate, objectMapper, acnGpMappingVOs);
    }

    @Test
    void testLoadAccToGPMappingFromMdms() {

        doReturn("pb").when(pspclIfixAdapterConfiguration).getTenantId();
        doReturn("http://localhost:8080").when(pspclIfixAdapterConfiguration).getEgovMdmsHost();
        doReturn("/egov/mdms/search").when(pspclIfixAdapterConfiguration).getEgovMdmsSearchUrl();

        ResponseEntity<MdmsResponse> responseEntity = new ResponseEntity<MdmsResponse>(HttpStatus.OK);

        doReturn(responseEntity).when(restTemplate)
                .postForEntity((String) any(), (Object) any(), any());

        mDMSClient.loadAccToGPMappingFromMdms();
    }

    @Test
    void testGetAcnGpMappingVOs() {
        assertTrue((mDMSClient).getAcnGpMappingVOs().isEmpty());
    }
}

