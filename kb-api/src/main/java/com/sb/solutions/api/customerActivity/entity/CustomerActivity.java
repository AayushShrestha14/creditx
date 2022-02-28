package com.sb.solutions.api.customerActivity.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.enums.ActivityType;
import com.sb.solutions.api.user.entity.User;

/**
 * @author : Rujan Maharjan on  9/18/2020
 **/
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerActivity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private CustomerInfo profile;

    @Column(name = "data", updatable = false)
    private String data;

    @Column(name = "description", updatable = false)
    private String description;

    @OneToOne
    private User modifiedBy;

    private ActivityType activityType;

    @Column(name = "activity", nullable = false, updatable = false)
    private Activity activity;

    @Column(name = "customer_loan_id", updatable = false)
    private Long customerLoanId;

    @CreatedDate
    @Column(name = "modified_on", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedOn;

}
