package org.egov.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SoapServiceRequestRepositoryTest {

    @Mock
    private SoapServiceRequestRepository soapServiceRequestRepository;

    String billReq = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "    <soap:Body>\n" +
            "        <GetBills xmlns=\"http://tempuri.org/\">\n" +
            "            <accountNo>3005754414</accountNo>\n" +
            "        </GetBills>\n" +
            "    </soap:Body>\n" +
            "</soap:Envelope>";

    String uri = "http://localhost:9093/fetch/pspcl/bill";

    @BeforeEach
    private void init() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchResult() throws IOException {
        HttpURLConnection httpConn = mock(HttpURLConnection.class);
        ObjectOutputStream out = mock(ObjectOutputStream.class);
        doReturn(out).when(httpConn).getOutputStream();
        Object result = soapServiceRequestRepository.fetchResult(billReq, uri);
        assertNull(result);
    }

    @Test
    void testFetchResultWithException() throws IOException {
        HttpURLConnection httpConn = mock(HttpURLConnection.class);
        ObjectOutputStream out = mock(ObjectOutputStream.class);
        doThrow(new RuntimeException()).when(httpConn).getOutputStream();
        Object result = soapServiceRequestRepository.fetchResult(billReq, uri);
        assertNull(result);
    }
}

