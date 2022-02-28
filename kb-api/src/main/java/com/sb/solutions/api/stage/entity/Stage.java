package com.sb.solutions.api.stage.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocAction;

/**
 * @author Sunil Babu Shrestha on 5/24/2019
 */
@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Audited
public class Stage extends BaseEntity<Long> {

    @OneToOne
    private User fromUser;

    @OneToOne
    private Role fromRole;

    @OneToOne
    private User toUser;

    @OneToOne
    private Role toRole;

    private DocAction docAction;

    private String comment;
}
