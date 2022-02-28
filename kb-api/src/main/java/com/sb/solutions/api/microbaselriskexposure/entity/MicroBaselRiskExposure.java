package com.sb.solutions.api.microbaselriskexposure.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author Mohammad Hussain
 * created on 2/16/2021
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MicroBaselRiskExposure extends BaseEntity<Long> {
    private String data;
}
