package com.sb.solutions.entity;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.stage.entity.Stage;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.utils.string.WordFormatter;
import com.sb.solutions.enums.CADDocAction;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author : Rujan Maharjan on  12/4/2020
 **/
@Entity
@Data
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class CadStage extends BaseEntity<Long> {

    @OneToOne
    private User fromUser;

    @OneToOne
    private Role fromRole;

    @OneToOne
    private User toUser;

    @OneToOne
    private Role toRole;

    private CADDocAction docAction;

    @JsonDeserialize(using = WordFormatter.class)
    private String comment;

}
