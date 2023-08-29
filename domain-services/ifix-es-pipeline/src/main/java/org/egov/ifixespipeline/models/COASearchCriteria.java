package org.egov.ifixespipeline.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

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
public class COASearchCriteria {
    @JsonProperty("Ids")
    @Valid
    private List<String> ids = null;

    @JsonProperty("tenantId")
    private String tenantId = null;

    @JsonProperty("majorHead")
    private String majorHead = null;

    @JsonProperty("subMajorHead")
    private String subMajorHead = null;

    @JsonProperty("minorHead")
    private String minorHead = null;

    @JsonProperty("subHead")
    private String subHead = null;

    @JsonProperty("groupHead")
    private String groupHead = null;

    @JsonProperty("objectHead")
    private String objectHead = null;

    @JsonProperty("coaCodes")
    private List<String> coaCodes = null;


    public COASearchCriteria addIdsItem(String idsItem) {
        if (this.ids == null) {
            this.ids = new ArrayList<>();
        }
        this.ids.add(idsItem);
        return this;
    }

    public boolean isEmpty() {
        return (StringUtils.isBlank(tenantId) && StringUtils.isBlank(majorHead) && StringUtils.isBlank(minorHead)
                && StringUtils.isBlank(subHead) && StringUtils.isBlank(subMajorHead) && StringUtils.isBlank(groupHead)
                && StringUtils.isBlank(objectHead) && (ids == null || ids.isEmpty()) && (coaCodes == null || coaCodes.isEmpty()));
    }

}
