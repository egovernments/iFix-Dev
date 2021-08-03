package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.COARequest;
import org.egov.web.models.ChartOfAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ChartOfAccountValidator {


    public void validateCreatePost(COARequest coaRequest) {
        log.info("Enter into ChartOfAccountValidator.validateCreatePost()");
        ChartOfAccount chartOfAccount = coaRequest.getChartOfAccount();
        Map<String, String> errorMap =  new HashMap<>();

        if(StringUtils.isBlank(chartOfAccount.getGroupHead())){
            errorMap.put("GROUP_HEAD","Group head Code is mandatory for chart of account");
        }
        if(StringUtils.isBlank(chartOfAccount.getMajorHead())){
            errorMap.put("MAJOR_HEAD","Major head Code is mandatory for chart of account");
        }
        if(StringUtils.isBlank(chartOfAccount.getMinorHead())){
            errorMap.put("MINOR_HEAD","Minor head Code is mandatory for chart of account");
        }
        if(StringUtils.isBlank(chartOfAccount.getSubHead())){
            errorMap.put("SUB_HEAD","Sub head Code is mandatory for chart of account");
        }
        if(StringUtils.isBlank(chartOfAccount.getObjectHead())){
            errorMap.put("OBJECT_HEAD","Object head Code is mandatory for chart of account");
        }
        if(StringUtils.isBlank(chartOfAccount.getSubMajorHead())){
            errorMap.put("SUB_MAJOR_HEAD","Sub Major Code is mandatory for chart of account");
        }

        //validate the tenant id is exist in the system or not
        if(StringUtils.isNotBlank(chartOfAccount.getTenantId())) {
            //call the Tenant Service for search, if doesn't exist add in the error map
            //errorMap.put("TENANT_ID","Tenant id doesn't exist in the system");
        }



        log.info("Exit from ChartOfAccountValidator.validateCreatePost()");
        if(!errorMap.isEmpty())
            throw new CustomException(errorMap);

    }
}
