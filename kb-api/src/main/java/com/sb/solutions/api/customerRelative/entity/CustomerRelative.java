package com.sb.solutions.api.customerRelative.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.utils.string.NameFormatter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
@Audited
public class CustomerRelative extends BaseEntity<Long> {

    private String title;

    private String customerRelation;

    @JsonDeserialize(using = NameFormatter.class)
    private String customerRelativeName;

    private String citizenshipNumber;

    private String citizenshipIssuedPlace;

    private Date citizenshipIssuedDate;

    @NotAudited
    private Integer age;

    @NotAudited
    private Double relativeNetWorth;
}
