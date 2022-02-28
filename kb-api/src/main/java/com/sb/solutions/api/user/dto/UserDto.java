package com.sb.solutions.api.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.authorization.dto.RoleDto;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.branch.dto.BranchDto;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {

    private Long id;
    private String username;
    private List<BranchDto> branchDtoList;
    private RoleDto role;
    private Boolean isDefaultCommittee = false;
    private Status status;

    private String name;
    private List<BranchDto> branch;
    private String signatureImage;
    private Integer isUnseenMsg;
    private RoleType roleType;

    public UserDto(Long id, String username, RoleType roleType) {
        this.id = id;
        this.username = username;
        this.roleType = roleType;
    }
}
