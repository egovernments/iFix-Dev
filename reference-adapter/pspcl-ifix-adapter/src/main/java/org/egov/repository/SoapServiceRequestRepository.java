package org.egov.repository;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class SoapServiceRequestRepository {


    public Object fetchResult(String req, String uri) {
        Document document;
        URL url;
        HttpURLConnection httpConn = null;
        String responseString;
        String outputString = "";
        OutputStream out;
        InputStreamReader isr = null;
        BufferedReader in = null;

        if (StringUtils.isNotBlank(req) && StringUtils.isNotBlank(uri)) {
            try {
                url = new URL(uri);
                httpConn = (HttpURLConnection) url.openConnection();

                byte[] buffer = req.getBytes();

                // Set the appropriate HTTP parameters.
                httpConn.setRequestProperty("Content-Length", String.valueOf(buffer.length));
                httpConn.setRequestProperty("Content-Type", "text/xml; charset=iso-8859-1");
                httpConn.setRequestMethod(HttpMethod.POST.name());
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);

                out = httpConn.getOutputStream();
                out.write(buffer);
                out.close();

                if (httpConn != null) {
                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                        log.error("PSPCL system is down, please try after sometime!", httpConn.getResponseMessage());
                        return null;
                    }
                    if (httpConn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        log.error("PSPCL request is invalid, please check the request file!", httpConn.getResponseMessage());
                        return null;
                    }
                }
                
                try {
                    isr = new InputStreamReader(httpConn.getInputStream());
                    in = new BufferedReader(isr);
                    while ((responseString = in.readLine()) != null) {
                        outputString = outputString + responseString;
                    }
                } catch (Exception e) {
                    log.error("Exception occurred while reading the response content from {} : {}", uri, e);
                    throw new CustomException("PSPCL_API_CALL",e.getMessage());
                } finally {
                    if (isr != null) {
                        isr.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
            } catch (Exception e) {
                log.error("Exception occurred while fetching the details from other system", e);
                throw new CustomException("PSPCL_API_CALL",e.getMessage());
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                    httpConn = null;
                }
            }
        }

        return outputString;
    }
}
