package com.sb.solutions.api.openingForm.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.LastModifiedDate;

import com.sb.solutions.api.accountType.entity.AccountType;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enums.AccountStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "opening_form")
public class OpeningForm{

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Branch branch;
    private Date requestedDate;
    private String fullName;
    private String customerPhotoPath;
    @ManyToOne
    private AccountType accountType;
    private AccountStatus status;
    @Column(columnDefinition = "text")
    private String customerDetailsJson;
    @Transient
    private OpeningAccount openingAccount;

    private String remark;

    private String accountNumber;

    @LastModifiedDate
    @JsonFormat(pattern = AppConstant.DATE_FORMAT)
    private Date lastFollowUp = new Date();

    @OneToOne(cascade = {CascadeType.MERGE , CascadeType.PERSIST})
    @JoinColumn(name = "last_follow_up_by")
    private User lastFollowUpBy;
}
