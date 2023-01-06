package org.egov.ifixespipeline.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.egov.common.contract.response.ResponseHeader;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class COAResponse {
    @JsonProperty("responseHeader")
    private ResponseHeader responseHeader = null;

    @JsonProperty("chartOfAccounts")
    @Valid
    private List<ChartOfAccount> chartOfAccounts = null;


    public COAResponse addChartOfAccountsItem(ChartOfAccount chartOfAccountsItem) {
        if (this.chartOfAccounts == null) {
            this.chartOfAccounts = new ArrayList<>();
        }
        this.chartOfAccounts.add(chartOfAccountsItem);
        return this;
    }

}
