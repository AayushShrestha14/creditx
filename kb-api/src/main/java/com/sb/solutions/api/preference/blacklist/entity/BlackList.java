package com.sb.solutions.api.preference.blacklist.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

/**
 * @author: Aashish shrestha on 20th. Feb, 2020
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BlackList extends BaseEntity<Long> {

    private String name;
    private String ref;
    private Character docType;
    private Status status = Status.ACTIVE;
}
