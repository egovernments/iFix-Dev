package org.egov.ifix.persistance;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "PspclEventDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PspclEventDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Size(max = 36)
    private String eventId;

    @Size(max = 64)
    private String tenantId;

    @Size(max = 16)
    private String eventType;

    private Boolean success;

    private String accountNo;

    private Double amount;

    @Size(max = 2000, min = 0)
    private String error;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String data;

    private Date createdDate;
}
