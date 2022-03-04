package com.sb.solutions.admin.role.dto;

import lombok.Data;

@Data
public class ChangePasswordAdminDto {

    private String username;
    private String newPassword;
    private String oldPassword;


}
