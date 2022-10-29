package org.egov.ifix.persistance;

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
 * 
 * Table to maintain project mapping between IFIX and its Client. 
 * Priority is given for clientCode . If with clientCode Single project found in IFIX it is used. If multiple found 
 * it will use the IfixId to filter down. As of now tenantId is not used. It is kept for future reference.
 * Later when Project Creation goes through adapter it is saved here  with same clientProjectCode
 *
 */
@Entity
@Table(name = "ifix_adapter_project_map")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMap {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id; 
	
	@Size(max=64)
	@UniqueElements
	@Column(name="clientprojectcode")
	private String clientProjectCode;
	
	@Size(max=64)
	@Column(name="ifixprojectid")
	private String iFixProjectId;
	
	@Size(max=64)
	@Column(name="tenantid")
	private String tenantId;
}
