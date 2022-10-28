package org.egov.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.egov.common.contract.request.RequestHeader;
import org.egov.common.contract.request.UserInfo;
import org.egov.config.PspclIfixAdapterConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;


@Component
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeaderUtil {

    @Autowired
    private PspclIfixAdapterConfiguration adapterConfiguration;

    public RequestHeader getRequestHeader() {
        RequestHeader requestHeader = new RequestHeader();
        requestHeader.setTs(Instant.now().toEpochMilli());

        return requestHeader;
    }

}

