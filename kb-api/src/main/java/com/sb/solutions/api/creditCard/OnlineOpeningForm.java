package com.sb.solutions.api.creditCard;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.AccountStatus;
import com.sb.solutions.core.enums.OnlineOpeningType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OnlineOpeningForm  extends BaseEntity<Long> {
    @ManyToOne
    private Branch branch;
    private Date requestedDate;
    private String fullName;
    private String customerPhotoPath;

    private AccountStatus status;

    @Column(columnDefinition = "text")
    private String customerDetailsJson;

    @Enumerated(EnumType.STRING)
    private OnlineOpeningType openingType;

    @LastModifiedDate
    @JsonFormat(pattern = AppConstant.DATE_FORMAT)
    private Date lastFollowUp = new Date();

    @OneToOne(cascade = {CascadeType.MERGE , CascadeType.PERSIST})
    @JoinColumn(name = "last_follow_up_by")
    private User lastFollowUpBy;
}
