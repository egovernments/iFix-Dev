package org.egov.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * The object contains all the search criteria of the fund
 */
@ApiModel(description = "The object contains all the search criteria of the fund")
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2021-08-02T16:24:12.742+05:30")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundSearchCriteria {
    @JsonProperty("fundIds")
    @Valid
    private List<String> fundIds = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("fundCode")
    @Valid
    private List<String> fundCode = null;

    @JsonProperty("subFundCode")
    @Valid
    private List<String> subFundCode = null;

    @JsonProperty("subFundIds")
    @Valid
    private List<String> subFundIds = null;


    public FundSearchCriteria addFundIdsItem(String fundIdsItem) {
        if (this.fundIds == null) {
            this.fundIds = new ArrayList<>();
        }
        this.fundIds.add(fundIdsItem);
        return this;
    }

    public FundSearchCriteria addFundCodeItem(String fundCodeItem) {
        if (this.fundCode == null) {
            this.fundCode = new ArrayList<>();
        }
        this.fundCode.add(fundCodeItem);
        return this;
    }

    public FundSearchCriteria addSubFundCodeItem(String subFundCodeItem) {
        if (this.subFundCode == null) {
            this.subFundCode = new ArrayList<>();
        }
        this.subFundCode.add(subFundCodeItem);
        return this;
    }

    public FundSearchCriteria addSubFundIdsItem(String subFundIdsItem) {
        if (this.subFundIds == null) {
            this.subFundIds = new ArrayList<>();
        }
        this.subFundIds.add(subFundIdsItem);
        return this;
    }

}

