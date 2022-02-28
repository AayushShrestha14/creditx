package com.sb.solutions.api.netTradingAssets.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * @author Amulya Shrestha on 10/15/2020
 **/

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class NetTradingAssets extends BaseEntity<Long> {
    private String data;
}
