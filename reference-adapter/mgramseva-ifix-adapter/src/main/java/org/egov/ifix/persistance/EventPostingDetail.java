package org.egov.ifix.persistance;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "EventPostingDetail")
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPostingDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Size(max=64)
	private String tenantId;
	
	
	@Size(max=64)
	private String eventId;
	
	@Size(max=64)
	private String ifixEventId;
	
	@Size(max=64)
	private String referenceId;
	
	@Size(max=16)
	private String eventType;
	
	@Size(max=16)
	private String status;
	
	@Size(max=4000,min=0)
	private String error;

	@Size(max=36)
	private String projectId;
	
	 
	@Lob
	private String record;
	
	private Date createdDate;
	
	private Date lastModifiedDate;
	
	

}
