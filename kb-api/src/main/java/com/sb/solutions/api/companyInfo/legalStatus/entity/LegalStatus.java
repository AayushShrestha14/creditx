package com.sb.solutions.api.companyInfo.legalStatus.entity;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.Pair;

import com.sb.solutions.api.company.entity.Company;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enitity.EntityValidator;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
@Audited
public class LegalStatus extends BaseEntity<Long> implements EntityValidator {

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne
    private Company corporateStructure;

    private String registeredOffice;

    private String registeredUnderAct;

    private Date registrationDate;

    private String panRegistrationOffice;

    private Date panRegistrationDate;

    private Date registrationExpiryDate;

    @Override
    public Pair<Boolean, String> valid() {
        final String validationMsg = "Company Info -None of Legal Status Section Field can be left empty except registered under act";
        Pair pair = Pair.of(Boolean.TRUE, "");
        Boolean anyAttributeNull = Stream.of(this.corporateStructure,
            this.registeredOffice,
            this.registrationDate, this.panRegistrationOffice,
            this.panRegistrationDate).anyMatch(Objects::isNull);
        if (anyAttributeNull) {
            pair = Pair.of(Boolean.FALSE,
                validationMsg);
        }
        return pair;
    }

}
