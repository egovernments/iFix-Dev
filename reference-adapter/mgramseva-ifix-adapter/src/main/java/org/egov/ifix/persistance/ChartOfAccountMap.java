package org.egov.ifix.persistance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * @author mani
 * This table is used for mapping COA between IFIX and its client
 * Here you can map IFIX coaCode directly which will work across the environment
 * or you can map finding the right id from the specific environment
 * Priority given for the iFixCoaCode . if iFixCoaCode not present id will be taken for consideration
 * COAs work irrespective of tenants so tenantId is  ignored . tenantId is only kept for future use
 * if both are missing mapping exception is thrown
 */

@Entity
@Table(name = "ifix_adapter_coa_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountMap {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Size(max = 64)
    @Column(name = "clientcode", unique = true)
    private String clientCode;

    @Size(max = 64)
    @Column(name = "ifixid")
    private String iFixId;

    @Size(max = 64)
    @Column(name = "ifixcoacode")
    private String iFixCoaCode;

    @Size(max = 64)
    @Column(name = "tenantid")
    private String tenantId;

}
