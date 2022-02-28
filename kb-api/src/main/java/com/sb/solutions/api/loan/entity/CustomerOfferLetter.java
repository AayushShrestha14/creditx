package com.sb.solutions.api.loan.entity;

import static com.sb.solutions.core.constant.AppConstant.DATE_FORMAT;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loan.OfferLetterStage;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerOfferLetter extends BaseEntity<Long> {


    private Boolean isOfferLetterIssued = false;

    private Boolean isOfferLetterApproved = false;

    private DocStatus docStatus = DocStatus.PENDING;

    @OneToOne
    private CustomerLoan customerLoan;

    @OneToOne(cascade = CascadeType.ALL)
    private OfferLetterStage offerLetterStage;

    private String offerLetterStageList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "offer_letter_path_customer_loan")
    private List<CustomerOfferLetterPath> customerOfferLetterPath;

    @Transient
    private List previousList;

    public List<LoanStageDto> getPreviousList() {
        if (this.getOfferLetterStageList() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                this.previousList = objectMapper.readValue(this.getOfferLetterStageList(),
                    typeFactory.constructCollectionType(List.class, LoanStageDto.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.previousList = new ArrayList();
        }
        return this.previousList;
    }

}
