package com.sb.solutions.admin.notificationmaster;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Elvin Shrestha on 4/15/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationMasterAdminDto extends BaseDto<Long> {

    private Status status;
}
