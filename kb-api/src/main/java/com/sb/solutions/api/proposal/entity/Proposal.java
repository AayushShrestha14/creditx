package com.sb.solutions.api.proposal.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.utils.NumberToWordsConverter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
@Audited
public class Proposal extends BaseEntity<Long> {

    private String data;
    private BigDecimal proposedLimit;
    private Double tenureDurationInMonths;
    private String checkedData;
    private BigDecimal existingLimit;
    private BigDecimal outStandingLimit;
    private Double collateralRequirement;
    private String limitExpiryMethod;
    private Double duration;
    private String condition;
    private String frequency;
    private Date dateOfExpiry;

    @NotAudited
    private Double cashMargin;
    @NotAudited
    private Double commissionPercentage;
    @NotAudited
    private String commissionFrequency;
    @NotAudited
    private Double couponRate;
    @NotAudited
    private Double premiumOnCouponRate;
    @NotAudited
    private String tenorOfEachDeal;
    @NotAudited
    private String cashMarginMethod;
    @NotAudited
    private BigDecimal enhanceLimitAmount;

    @NotAudited
    @JsonProperty("groupExposure")
    private String groupExposure;


    @Transient
    private String proposedAmountInWords;

    public String getProposedAmountInWords() {
        try {
            return NumberToWordsConverter
                .calculateAmountInWords(String.valueOf(this.getProposedLimit()));
        } catch (Exception e) {
            return null;
        }
    }

}
