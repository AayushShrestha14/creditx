package com.sb.solutions.web.preference.notificationMaster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;

/**
 * @author Elvin Shrestha on 4/15/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationMasterDto extends BaseDto<Long> {

    private Status status;
}
