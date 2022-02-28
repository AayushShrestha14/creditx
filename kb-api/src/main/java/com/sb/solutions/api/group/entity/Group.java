package com.sb.solutions.api.group.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.sb.solutions.api.valuator.entity.Valuator;
import com.sb.solutions.core.enitity.BaseEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "groups")
public class Group extends BaseEntity<Long> {

    private String data;

    @ManyToMany
    private List<Valuator> valuator;

}
