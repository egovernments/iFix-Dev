package org.egov.ifix.persistance;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.UniqueElements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author mani
 *   This table is used for mapping COA between IFIX and its client 
 *   Here you can map IFIX coaCode directly which will work across the environment
 *   or you can map finding the right id from the specific environment
 *   Priority given for the iFixCoaCode . if iFixCoaCode not present id will be taken for consideration
 *   COAs work irrespective of tenants so tenantId is  ignored . tenantId is only kept for future use
 *   if both are missing mapping exception is thrown 
 */

@Entity
@Table(name = "ifix_adapter_coa_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartOfAccountMap {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id; 
	
	@Size(max=64)
	@UniqueElements
	@Column(name="clientcode")
	private String clientCode;
	
	@Size(max=64)
	@Column(name="ifixid")
	private String iFixId;
	
	@Size(max=64)
	@Column(name="ifixcoacode")
	private String iFixCoaCode;
	
	@Size(max=64)
	@Column(name="tenantid")
	private String tenantId;

}
