package org.egov.ifix.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.egov.ifix.cache.AdapterCache;
import org.egov.ifix.exception.GenericCustomException;
import org.egov.ifix.models.mgramseva.*;
import org.egov.ifix.repository.MgramsevaVendorRepository;
import org.egov.ifix.service.AuthTokenService;
import org.egov.ifix.service.MgramsevaVendorService;
import org.egov.ifix.utils.ApplicationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.egov.ifix.utils.EventConstants.*;

@Slf4j
@Service
public class MgramsevaVendorServiceImpl implements MgramsevaVendorService {

    @Autowired
    private AdapterCache<String> vendorCache;

    @Autowired
    private MgramsevaVendorRepository mgramsevaVendorRepository;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    /**
     * @param tenantId
     * @param name
     * @return
     */
    @Override
    public String getVendorIdByTenantId(@NotNull String tenantId, @NotNull String name) {
        String vendorKeyName = tenantId + "_" + name;
        String cachedVendorId = vendorCache.getValue(vendorKeyName);

        if (StringUtils.isEmpty(cachedVendorId)) {
            Optional<VendorDTO> vendorOptional = Optional.empty();
            Optional<SearchVendorResponseDTO> searchVendorResponseDTOOptional = mgramsevaVendorRepository
                    .searchVendor(tenantId, name);

            if (searchVendorResponseDTOOptional.isPresent()
                    && searchVendorResponseDTOOptional.get().getVendor() != null
                    && !searchVendorResponseDTOOptional.get().getVendor().isEmpty()) {

                vendorOptional = searchVendorResponseDTOOptional.get().getVendor().stream().findFirst();
            } else {
                Optional<VendorCreateResponseDTO> vendorCreateResponseDTOOptional = mgramsevaVendorRepository
                        .createVendor(wrapVendorCreateRequestData(tenantId, name));

                if (vendorCreateResponseDTOOptional.isPresent()
                        && vendorCreateResponseDTOOptional.get().getVendor() != null
                        && !vendorCreateResponseDTOOptional.get().getVendor().isEmpty()) {

                    vendorOptional = vendorCreateResponseDTOOptional.get().getVendor().stream().findFirst();
                }else {
                    log.error("Unable to create vendor in vendor service");
                    throw new GenericCustomException(MGRAMSEVA_VENDOR_ID, "Unable to create vendor in vendor service");
                }
            }
            if (vendorOptional.isPresent() && !StringUtils.isEmpty(vendorOptional.get().getId())) {
                vendorCache.putValue(vendorKeyName, vendorOptional.get().getId());
                cachedVendorId = vendorOptional.get().getId();
            } else {
                log.error("Vendor does not exit in the vendor list");
                throw new GenericCustomException(MGRAMSEVA_VENDOR_ID, "Vendor does not exit in the vendor list");
            }
        } else {
            log.info(LOG_INFO_PREFIX + "got vendor from Cache" + cachedVendorId);
        }

        return cachedVendorId;
    }


    /**
     * @param tenantId
     * @param name
     * @return
     */
    public @NotNull VendorCreateRequestDTO wrapVendorCreateRequestData(@NotNull String tenantId, @NotNull String name) {
        RoleDTO role = new RoleDTO();
        role.setTenantId(tenantId);
        role.setName(OWNER_ROLE_CITIZEN);
        role.setCode(OWNER_ROLE_CITIZEN);

        OwnerDTO owner = OwnerDTO.builder().build();
        owner.setTenantId(tenantId);
        owner.setName(name);
        owner.setFatherOrHusbandName(applicationConfiguration.getVendorOwnerFatherHusbandName());
        owner.setRelationship(applicationConfiguration.getVendorOwnerRelationship());
        owner.setGender(applicationConfiguration.getVendorOwnerGender());
        owner.setDob(Long.parseLong(applicationConfiguration.getVendorOwnerDob()));
        owner.setEmailId(applicationConfiguration.getVendorOwnerEmailId());
//      It is not fixed yet, it's just providing random mobile no with 10 digit. Needs to be implemented by user info.
        owner.setMobileNumber(
                String.valueOf(ThreadLocalRandom.current().nextLong(10000_11111L, 10000_99999L)));
        owner.setRoles(Collections.singletonList(role));

        LocalityDTO locality = LocalityDTO.builder().build();
        locality.setCode(applicationConfiguration.getVendorOwnerLocalityCode());

        AddressDTO address = AddressDTO.builder().build();
        address.setTenantId(tenantId);
        address.setLocality(locality);

        VendorDTO vendor = VendorDTO.builder().build();
        vendor.setTenantId(tenantId);
        vendor.setName(name);
        vendor.setVehicles(Collections.emptyList());
        vendor.setDrivers(Collections.emptyList());
        vendor.setSource(name);
        vendor.setAddress(address);
        vendor.setOwner(owner);

        return VendorCreateRequestDTO.builder()
                .requestInfo(getMgramsevaRequestInfo())
                .vendor(vendor)
                .build();
    }


    /**
     * @return
     */
    private RequestInfoDTO getMgramsevaRequestInfo() {
        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setApiId(MGRAMSEVA_CHALLAN_API_ID);
        requestInfoDTO.setVer(MGRAMSEVA_CHALLAN_VERSION);
        requestInfoDTO.setAction(MGRAMSEVA_CHALLAN_CREATE_ACTION);
        requestInfoDTO.setDid(MGRAMSEVA_CHALLAN_DID);
        requestInfoDTO.setMsgId(MGRAMSEVA_CHALLAN_MSG_ID);
        requestInfoDTO.setAuthToken(authTokenService.getMgramsevaAccessToken());

        return requestInfoDTO;
    }
}
