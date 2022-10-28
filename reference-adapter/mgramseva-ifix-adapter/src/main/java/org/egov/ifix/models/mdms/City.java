package org.egov.ifix.models.mdms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {

    private String name;
    private String code;
    private String projectId;

}