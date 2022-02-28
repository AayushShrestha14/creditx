package com.sb.solutions.api.preference.notificationMaster.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.NotificationMasterType;
import com.sb.solutions.core.enums.Status;

/**
 * @author Aashish shrestha on 10th. March, 2020
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationMaster extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    private NotificationMasterType notificationKey;

    private int value;

    private Status status;
}
