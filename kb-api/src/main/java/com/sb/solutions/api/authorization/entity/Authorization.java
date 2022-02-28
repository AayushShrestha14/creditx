package com.sb.solutions.api.authorization.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "authorization")
public class Authorization extends BaseEntity<Long> implements Serializable {

    private String name;
    private String value;
    private String key;
}
