package org.egov.ifix.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfiguration {

    @Value("${kafka.topics.ifix.adaptor.mapper}")
    private String mapperTopicName;

    @Value("${kafka.topics.ifix.adaptor.error}")
    private String errorTopicName;

    @Value("${kafka.topics.ifix.adaptor.http.error}")
    private String httpErrorTopicName;

    @Value("${state.goverment.code}")
    private String tenantId;

    @Value("${keycloak.credentials.clientid}")
    private String clientId;

    @Value("${keycloak.credentials.clientsecret}")
    private String clientSecret;

    @Value("${keycloak.credentials.granttype}")
    private String grantType;

    @Value("${keycloak.host}")
    private String keyCloakHost;

    @Value("${keycloak.token.endpoint}")
    private String keyCloakAuthApi;

    @Value("${ifix.host}")
    private String ifixHost;

    @Value("${ifix.event.endpoint}")
    private String ifixEventEndpoint;

    @Value("${ifix.event.search.endpoint}")
    private String ifixEventSearchEndpoint;

    @Value("${ifix.coa.search.endpoint}")
    private String coaSearchEndpoint;

    @Value("${ifix.project.search.endpoint}")
    private String projectSearchEndpoint;

    @Value("${ifix.department.entity.host}")
    private String departmentEntityHost;

    @Value("${ifix.department.entity.context.path}")
    private String departmentEntityContextPath;

    @Value("${ifix.department.entity.search.path}")
    private String departmentEntitySearchPath;

    @Value("${pspcl.ifix.event.receiver.name}")
    private String pspclIfixEventReceiverName;

    @Value("${ifix.fiscal.event.search.time.overlap.minutes}")
    private String ifixFiscalEventSearchTimeOverlapMinutes;

    @Value("${adapter.master.data.host}")
    private String adapterMasterDataHost;

    @Value("${adapter.master.data.context.path}")
    private String adapterMasterDataContextPath;

    @Value("${adapter.master.department.search.path}")
    private String adapterMasterDepartmentSearchPath;

    @Value("${adapter.master.expenditure.search.path}")
    private String adapterMasterExpenditureSearchPath;

    @Value("${adapter.master.project.search.path}")
    private String adapterMasterProjectSearchPath;

    @Value("${mgramseva.host}")
    private String mgramsevaHost;

    @Value("${mgramseva.oauth.access.token.endpoint}")
    private String mgramsevaOauthAccessTokenEndpoint;

    @Value("${mgramseva.create.challan.endpoint}")
    private String mgramsevaCreateChallanEndpoint;

    @Value("${mgramseva.update.challan.endpoint}")
    private String mgramsevaUpdateChallanEndpoint;

    @Value("${mgramseva.search.challan.endpoint}")
    private String mgramsevaSearchChallanEndpoint;

    @Value("${mgramseva.vendor.search.endpoint}")
    private String mgramsevaVendorSearchEndpoint;

    @Value("${mgramseva.vendor.create.endpoint}")
    private String mgramsevaVendorCreateEndpoint;

    @Value("${mgramseva.pspcl.business.service}")
    private String mgramsevaPspclBusinessService;

    @Value("${mgramseva.pspcl.consumer.type}")
    private String mgramsevaPspclConsumerType;

    @Value("${mgramseva.pspcl.typeOfExpense}")
    private String mgramsevaPspclTypeOfExpense;

    @Value("${mgramseva.pspcl.vendor.name}")
    private String mgramsevaPspclVendorName;

    @Value("${mgramseva.pspcl.tax.head.code}")
    private String mgramsevaPspclTaxHeadCode;

    @Value("${mdms.search.endpoint}")
    private String mdmsSearchEndpoint;

    @Value("${mgramseva.billing.service.fetch.bill.endpoint}")
    private String mgramsevaBillingServiceFetchBillEndpoint;

    @Value("${mgramseva.collection.service.payments.create.endpoint}")
    private String mgramsevaCollectionServicePaymentsCreateEndpoint;

    @Value("${mgramseva.basic.authorization.base64.value}")
    private String mgramsevaBasicAuthorizationBase64Value;

    @Value("${mgramseva.oauth.token.username}")
    private String mgramsevaOauthTokenUsername;

    @Value("${mgramseva.oauth.token.password}")
    private String mgramsevaOauthTokenPassword;

    @Value("${mgramseva.oauth.token.scope}")
    private String mgramsevaOauthTokenScope;

    @Value("${mgramseva.oauth.token.grantType}")
    private String mgramsevaOauthTokenGrantType;

    @Value("${mgramseva.oauth.token.tenantId}")
    private String mgramsevaOauthTokenTenantId;

    @Value("${mgramseva.oauth.token.userType}")
    private String mgramsevaOauthTokenUserType;

    @Value("${vendor.owner.father.husband.name}")
    private String vendorOwnerFatherHusbandName;

    @Value("${vendor.owner.relationship}")
    private String vendorOwnerRelationship;

    @Value("${vendor.owner.gender}")
    private String vendorOwnerGender;

    @Value("${vendor.owner.dob}")
    private String vendorOwnerDob;

    @Value("${vendor.owner.emailId}")
    private String vendorOwnerEmailId;

    @Value("${vendor.owner.locality.code}")
    private String vendorOwnerLocalityCode;

    @Value("${samplejob.frequency}")
    private String sampleJobFrequency;

}
