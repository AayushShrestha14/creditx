package com.sb.solutions.api.companyInfo.model.entity;

import com.sb.solutions.api.companyInfo.capital.entity.Capital;
import com.sb.solutions.api.companyInfo.companyLocations.entity.CompanyLocations;
import com.sb.solutions.api.companyInfo.legalStatus.entity.LegalStatus;
import com.sb.solutions.api.companyInfo.model.enums.MicroCustomerType;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enitity.EntityValidator;
import com.sb.solutions.core.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.Pair;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
@Audited
public class CompanyInfo extends BaseEntity<Long> implements EntityValidator {

    @OneToOne(cascade = CascadeType.ALL)
    private LegalStatus legalStatus;

    @OneToOne(cascade = CascadeType.ALL)
    private Capital capital;

    private String companyName;
    private String registrationNumber;
    private Date establishmentDate;
    private BusinessType businessType;
    private String panNumber;
    private String issuePlace;
    private String email;
    private String contactNum;
    private String additionalCompanyInfo;
    private String successionPlanning;

    @Transient
    private String bankingRelationship;
    @Transient
    private String customerCode;
    private String industryGrowth;
    private String marketCompetition;
    private String experience;
    private String succession;
    private String businessAndIndustry;


    private String companyJsonData;

    @NotAudited
    @OneToOne(cascade = CascadeType.ALL)
    private CompanyLocations companyLocations;

    private String contactPersons;

    @NotAudited
    private String withinLimitRemarks;

    @NotAudited
    private String landLineNumber;

    @Transient
    private ClientType clientType;

    @Transient
    private String subsectorDetail;

    @NotAudited
    private String businessGiven;

    @NotAudited
    private Boolean isMicroCustomer;

    @NotAudited
    private MicroCustomerType microCustomerType;

    @Override
    public Pair<Boolean, String> valid() {
        Boolean validator = Boolean.TRUE;
        StringBuilder validationMsg = new StringBuilder();
        if (StringUtils.isEmpty(this.companyName)
            || StringUtils.isEmpty(this.panNumber)
            || null == this.establishmentDate) {
            validator = Boolean.FALSE;
            validationMsg.append(
                "Company Name, PAN number, Establishment Date - any of these field cannot be left blank.");
        }
        // validate legal status field
        if (!this.legalStatus.isValid()) {
            validator = Boolean.FALSE;
            validationMsg.append(legalStatus.getValidationMsg());
        }
        return Pair.of(validator, validationMsg.toString());
    }
}
