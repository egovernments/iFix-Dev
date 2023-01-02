package org.egov.ifix.models;


import lombok.Data;

/**
 * It contains error data and its related information,
 * it tries to encapsulate meta-data of error data.
 *
 *
 *
 * This model provides information about What, why, where, when
 * and description of error behaviour.
 */
@Data
public class ErrorDataModel {

    /**
     * UUID is preference for id value.
     * In some other scenario, it can support other kind of id values.
     */
    private String id;

    private String data;

    private String attributeName;

    private String attributeValue;

    private String dataName;

    private String errorType;

    private String status;

    private String message;

    private String origin;

    private String destination;

    private String createdBy;

    private Long createdTime;


}
