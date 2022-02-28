package com.sb.solutions.api.authorization.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoleDto {

    private Long id;

    private String name;

    private List<UserDto> userDtoList;

    private String roleName;

    private RoleType roleType;

    private RoleAccess roleAccess;

    private String authorityLabel;

    private Status status;

    public RoleDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
