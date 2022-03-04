package com.sb.solutions.web.common.stage.mapper;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.user.dto.RoleDto;
import com.sb.solutions.web.user.dto.UserDto;

/**
 * @author Rujan Maharjan on 6/12/2019
 */

@Component
public class StageMapper {

    private static final Logger logger = LoggerFactory.getLogger(StageMapper.class);

    private final UserService userService;

    public StageMapper(@Autowired UserService userService) {
        this.userService = userService;
    }


    public <T> T mapper(StageDto stageDto, List previousList, Class<T> classType, Long createdBy,
        StageDto currentStage, UserDto currentUser, CustomerLoan customerLoan) {
        ObjectMapper objectMapper = new ObjectMapper();
        User loggedUser = userService.getAuthenticatedUser();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        currentStage.setDocAction(stageDto.getDocAction());
        currentStage.setComment(stageDto.getComment());

        if (stageDto.getDocAction() != DocAction.TRANSFER && stageDto.getDocAction()
            != DocAction.PULLED && stageDto.getDocAction() != DocAction.RE_INITIATE) {
            currentStage.setFromUser(currentUser);
            currentStage.setFromRole(currentUser.getRole());
        } else {
            currentStage.setFromUser(currentStage.getToUser());
            currentStage.setFromRole(currentStage.getToRole());
            currentStage.setComment(
                (stageDto.getDocAction() == DocAction.RE_INITIATE ? "Re-initiated by "
                    : "Transfer By ")
                    + loggedUser.getName() + "(" + loggedUser.getRole().getRoleName() + "). "
                    + stageDto.getComment());
        }
        if (stageDto.getDocAction() == DocAction.PULLED) {
            currentStage.setComment("PULLED");
            currentStage.setToUser(currentUser);
            currentStage.setToRole(currentUser.getRole());
            logger.info("Pulled document {}", customerLoan, currentStage);
        } else {
            currentStage.setToUser(stageDto.getToUser());
            currentStage.setToRole(stageDto.getToRole());
        }
        if (stageDto.getDocAction() == DocAction.BACKWARD
            || stageDto.getDocAction() == DocAction.RE_INITIATE) {
            if (stageDto.getDocAction() == DocAction.RE_INITIATE
                && currentUser.getRole().getRoleType() == RoleType.MAKER) {
                // if current re-initiating user is maker user
                currentStage.setToUser(currentUser);
                currentStage.setToRole(currentUser.getRole());
            } else {
                currentStage = this
                    .sendBackward(previousList, currentStage, currentUser, createdBy, customerLoan);
            }
        }
        if (stageDto.getDocAction() == DocAction.APPROVED
            || stageDto.getDocAction() == DocAction.CLOSED
            || stageDto.getDocAction() == DocAction.REJECT
            || stageDto.getDocAction() == DocAction.NOTED) {
            if (stageDto.getDocAction() == DocAction.APPROVED) {
                currentStage.setLastModifiedAt(new Date());
            }
            currentStage = this.approvedCloseReject(currentStage, currentUser);
        }
        return objectMapper.convertValue(currentStage, classType);
    }

    private StageDto sendBackward(List previousList, StageDto currentStage, UserDto currentUser,
        Long createdBy, CustomerLoan customerLoan) {
        List<User> makers = userService
            .findByRoleTypeAndBranchIdAndStatusActive(RoleType.MAKER,
                customerLoan.getBranch().getId());
        logger.info("Sending backward loan id : {}", customerLoan.getId());
        if (makers == null || makers.isEmpty()) {
            throw new RuntimeException("No active Maker User Exists");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        int previousListSize = previousList.size();
        if (makers.size() == 1) {
            // single maker user exists
            logger.info("Return : Single maker user exists");
            return setUserDtoAndRoleDtoToCurrentStage(currentStage,
                objectMapper.convertValue(makers.get(0), UserDto.class),
                objectMapper.convertValue(makers.get(0).getRole(), RoleDto.class));
        }
        for (int i = previousListSize - 1; i >= 0; i--) {
            StageDto stage = objectMapper.convertValue(previousList.get(i), StageDto.class);
            Optional<UserDto> userDto = getActiveMakerOrNull(stage.getToUser(), stage.getToRole(),
                makers);
            if (userDto.isPresent()) {
                logger.info("Return : ToRole maker and active");
                return setUserDtoAndRoleDtoToCurrentStage(currentStage, stage.getToUser(),
                    stage.getToRole());
            }
            userDto = getActiveMakerOrNull(stage.getFromUser(), stage.getFromRole(), makers);
            if (userDto.isPresent()) {
                logger.info("Return : FromRole maker and active");
                return setUserDtoAndRoleDtoToCurrentStage(currentStage, stage.getFromUser(),
                    stage.getFromRole());
            }
        }
        // no active maker found in the previous stages, set random active maker
        logger.info("Return : No active maker exist in stages. Set random active maker.");
        return setUserDtoAndRoleDtoToCurrentStage(currentStage,
            objectMapper.convertValue(makers.get(0), UserDto.class),
            objectMapper.convertValue(makers.get(0).getRole(), RoleDto.class));
    }

    private Optional<UserDto> getActiveMakerOrNull(UserDto userDto, RoleDto roleDto,
        List<User> makers) {
        logger.info("Return : userDto.getId= {}", userDto.getId());
        return ((roleDto.getRoleType() == RoleType.MAKER) && (makers.stream()
            .anyMatch(user -> Objects.equals(user.getId().longValue(), userDto.getId().longValue())))) ? Optional.of(userDto)
            : Optional.empty();
    }

    private StageDto setUserDtoAndRoleDtoToCurrentStage(StageDto currentStage, UserDto userDto,
        RoleDto roleDto) {
        currentStage.setToUser(userDto);
        currentStage.setToRole(roleDto);
        return currentStage;
    }

    private StageDto approvedCloseReject(StageDto currentStage, UserDto currentUser) {
        currentStage.setToRole(currentUser.getRole());
        currentStage.setToUser(currentUser);
        return currentStage;
    }
}
