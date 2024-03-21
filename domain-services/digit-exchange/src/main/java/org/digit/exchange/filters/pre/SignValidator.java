package org.digit.exchange.filters.pre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digit.exchange.config.AppConfig;
import org.digit.exchange.model.RequestMessage;
import org.digit.exchange.service.SecurityService;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SignValidator implements Filter {

    private ObjectMapper objectMapper;
    private AppConfig appConfig;
    private SecurityService securityService;
    public SignValidator(ObjectMapper objectMapper, AppConfig config, SecurityService securityService) {
        this.objectMapper = objectMapper;
        this.appConfig = config;
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // Check if the request URI contains the context path
        String contextPath = httpRequest.getContextPath();
        // Check if the request URI matches the APIs you want to filter
        if (requestURI.startsWith(contextPath +  "/v1/exchange") && appConfig.isAuthEnabledExternalService()) {
            // Perform database operations or request validation here
            String requestBody = httpRequest.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);

            // Convert request body to your model
            RequestMessage requestMessage = objectMapper.readValue(requestBody, RequestMessage.class);
            try {
                // Validate signature if This is receiver endpoint
                if (securityService.isSameDomain(requestMessage.getHeader().getReceiverId(), this.appConfig.getDomain())) {
                    securityService.validateSignatureOfRequest(requestMessage);
                }
            } catch (Exception e) {
                throw new CustomException("Invalid Signature", "Signature is not valid");
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }



}
