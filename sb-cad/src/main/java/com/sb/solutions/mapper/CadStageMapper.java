package com.sb.solutions.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.core.NoContentException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.service.RoleService;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.dto.CadStageDto;
import com.sb.solutions.dto.StageDto;
import com.sb.solutions.entity.CadStage;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.enums.CADDocAction;
import com.sb.solutions.enums.CadDocStatus;

/**
 * @author : Rujan Maharjan on  12/7/2020
 **/

@Component
@Slf4j
public class CadStageMapper {

    private final ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper().
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));

    private final UserService userService;

    private final RoleService roleService;

    private static final String USER_ID = "userId";
    private static final String ROLE_ID = "roleId";

    public CadStageMapper(UserService userService,
        RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public String addPresentStageToPreviousList(List previousList, CadStage cadStage) {
        List previousListTemp = new ArrayList();
        if (cadStage != null) {

            Map<String, String> tempCadStage = objectMapper
                .convertValue(cadStage, Map.class);
            try {
                if (!ObjectUtils.isEmpty(previousList)) {
                    previousList.forEach(p -> {
                        try {
                            Map<String, String> previous = objectMapper.convertValue(p, Map.class);

                            previousListTemp.add(objectMapper.writeValueAsString(previous));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to handle JSON data");
                        }
                    });
                }
                String jsonValue = objectMapper.writeValueAsString(tempCadStage);
                previousListTemp.add(jsonValue);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to Get Stage data");
            }
        }
        return previousListTemp.toString();
    }


    public StageDto cadAction(CadStageDto requestedStage,
        CustomerApprovedLoanCadDocumentation oldData, User currentUser) {
        StageDto stageDto = new StageDto();
        stageDto.setPreviousList(
            addPresentStageToPreviousList(oldData.getPreviousList(), oldData.getCadCurrentStage()));
        CadStage cadStage = oldData.getCadCurrentStage();

        cadStage.setComment(requestedStage.getComment());
        User user = new User();
        Role role = new Role();
        switch (requestedStage.getDocAction()) {
            case FORWARD:
                cadStage.setFromRole(currentUser.getRole());
                cadStage.setFromUser(currentUser);
                role.setId(requestedStage.getToRole().getId());
                cadStage.setDocAction(CADDocAction.FORWARD);
                stageDto.setCadDocStatus(requestedStage.getDocumentStatus());
                final Role role1 = roleService.findOne(role.getId());
                if (role1.getRoleType().equals(RoleType.CAD_LEGAL)) {
                    stageDto.setCadDocStatus(CadDocStatus.LEGAL_PENDING);
                    cadStage.setToUser(null);
                } else {
                    user.setId(requestedStage.getToUser().getId());
                    cadStage.setToUser(user);
                }

                cadStage.setToRole(role);
                break;
            case BACKWARD:
                if ((cadStage.getDocAction().equals(CADDocAction.ASSIGNED)) || cadStage
                    .getFromRole().getRoleType().equals(RoleType.CAD_SUPERVISOR) || cadStage
                    .getFromRole().getRoleType().equals(RoleType.CAD_ADMIN)) {
                    Map<String, Long> cadMaker = this
                        .getCADMaker(oldData.getCadStageList(),
                            oldData.getLoanHolder().getBranch().getId());
                    if (ObjectUtils.isEmpty(cadMaker.get(USER_ID))) {
                        cadMaker = this
                            .getLoanMaker(
                                oldData.getAssignedLoan().get(0).getPreviousStageList(),
                                oldData.getAssignedLoan().get(0).getBranch().getId());
                    }
                    user.setId(cadMaker.get(USER_ID));
                    role.setId(cadMaker.get(ROLE_ID));
                    cadStage.setToUser(user);
                    cadStage.setToRole(role);

                } else {
                    if (requestedStage.getIsBackwardForMaker()) {
                        Map<String, Long> cadMaker = this
                            .getCADMaker(oldData.getCadStageList(),
                                oldData.getLoanHolder().getBranch().getId());
                        if (ObjectUtils.isEmpty(cadMaker.get(USER_ID))) {
                            cadMaker = this
                                .getLoanMaker(
                                    oldData.getAssignedLoan().get(0).getPreviousStageList(),
                                    oldData.getAssignedLoan().get(0).getBranch().getId());
                        }
                        user.setId(cadMaker.get(USER_ID));
                        role.setId(cadMaker.get(ROLE_ID));
                        cadStage.setToUser(user);
                        cadStage.setToRole(role);
                    } else {
                        cadStage.setToUser(cadStage.getFromUser());
                        cadStage.setToRole(cadStage.getFromRole());
                    }
                }
                cadStage.setDocAction(CADDocAction.BACKWARD);
                final Role fRole = roleService.findOne(cadStage.getFromRole().getId());
                if (fRole.getRoleType().equals(RoleType.CAD_LEGAL)) {
                    stageDto.setCadDocStatus(CadDocStatus.LEGAL_PENDING);
                } else {
                    stageDto.setCadDocStatus(requestedStage.getDocumentStatus());
                }
                cadStage.setFromRole(currentUser.getRole());
                cadStage.setFromUser(currentUser);

                break;
            case OFFER_APPROVED:
                cadStage.setFromRole(currentUser.getRole());
                cadStage.setFromUser(currentUser);
                cadStage.setDocAction(CADDocAction.OFFER_APPROVED);
                stageDto.setCadDocStatus(CadDocStatus.OFFER_APPROVED);

                if (requestedStage.getCustomApproveSelection()) {
                    User u1 = userService.findOne(requestedStage.getToUser().getId());
                    cadStage.setToUser(u1);
                    cadStage.setToRole(u1.getRole());
                } else {
                    //set RM
                    Map<String, Long> cadMaker = this
                        .getCADMaker(oldData.getCadStageList(),
                            oldData.getLoanHolder().getBranch().getId());
                    if (ObjectUtils.isEmpty(cadMaker.get(USER_ID))) {
                        cadMaker = this
                            .getLoanMaker(
                                oldData.getAssignedLoan().get(0).getPreviousStageList(),
                                oldData.getAssignedLoan().get(0).getBranch().getId());
                    }
                    user.setId(cadMaker.get(USER_ID));
                    role.setId(cadMaker.get(ROLE_ID));

                    cadStage.setToUser(user);
                    cadStage.setToRole(role);
                }
                break;
            case LEGAL_APPROVED:
                cadStage.setFromRole(currentUser.getRole());
                cadStage.setFromUser(currentUser);
                cadStage.setDocAction(CADDocAction.LEGAL_APPROVED);
                stageDto.setCadDocStatus(CadDocStatus.DISBURSEMENT_PENDING);
                //set to user CAD
                Map<String, Long> cadUser = this
                    .getCADUser(oldData.getPreviousList(),
                        oldData.getLoanHolder().getBranch().getId());
                role.setId(cadUser.get(ROLE_ID));
                cadStage.setToUser(null);
                cadStage.setToRole(role);
                break;
            case DISBURSEMENT_APPROVED:
                cadStage.setFromRole(currentUser.getRole());
                cadStage.setFromUser(currentUser);
                stageDto.setCadDocStatus(CadDocStatus.DISBURSEMENT_APPROVED);
                cadStage.setDocAction(CADDocAction.DISBURSEMENT_APPROVED);
                if (requestedStage.getCustomApproveSelection()) {
                    User u1 = userService.findOne(requestedStage.getToUser().getId());
                    cadStage.setToUser(u1);
                    cadStage.setToRole(u1.getRole());
                } else {
                    Map<String, Long> cadMaker1 = this
                        .getCADMaker(oldData.getCadStageList(),
                            oldData.getLoanHolder().getBranch().getId());
                    if (ObjectUtils.isEmpty(cadMaker1.get(USER_ID))) {
                        cadMaker1 = this
                            .getLoanMaker(
                                oldData.getAssignedLoan().get(0).getPreviousStageList(),
                                oldData.getAssignedLoan().get(0).getBranch().getId());
                    }
                    user.setId(cadMaker1.get(USER_ID));
                    role.setId(cadMaker1.get(ROLE_ID));
                    cadStage.setToUser(user);
                    cadStage.setToRole(role);
                }
                break;

        }
        stageDto.setCadStage(cadStage);
        return stageDto;
    }


    private Map<String, Long> getLoanMaker(String list, Long branchID) {
        Map<String, Long> map = new HashMap<>();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        try {
            List<LoanStageDto> mapPreviousList = objectMapper.readValue(list,
                typeFactory.constructCollectionType(List.class, LoanStageDto.class));
            List<LoanStageDto> makerList = mapPreviousList.stream()
                .filter(a -> a.getFromRole().getRoleType().equals(RoleType.MAKER)).collect(
                    Collectors.toList());
            final List<User> users = userService
                .findByRoleAndBranchId(makerList.get(0).getFromRole().getId(), branchID);
            final List<User> activeUser = users.stream()
                .filter(f -> f.getStatus().equals(Status.ACTIVE)).collect(
                    Collectors.toList());
            final List<Long> userIdList = activeUser.stream().map(User::getId)
                .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIdList)) {
                throw new NoContentException("no Initiator User Found");
            } else if (userIdList.contains(makerList.get(0).getFromUser().getId())) {
                map.put(USER_ID, makerList.get(0).getFromUser().getId());
                map.put(ROLE_ID, makerList.get(0).getFromRole().getId());
            } else {

                map.put(USER_ID, users.get(0).getId());
                map.put(ROLE_ID, users.get(0).getRole().getId());
            }


        } catch (Exception e) {
            log.error("unable to get users for backward ", e);
            final List<User> usersMaker = userService
                .findByRoleTypeAndBranchIdAndStatusActive(RoleType.MAKER, branchID);
            if (!usersMaker.isEmpty()) {
                map.put(USER_ID, usersMaker.get(0).getId());
                map.put(ROLE_ID, usersMaker.get(0).getRole().getId());
            }
        }
        return map;
    }


    private Map<String, Long> getCADMaker(String list, Long branchID) {
        Map<String, Long> map = new HashMap<>();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        try {
            List<CadStage> mapPreviousList = objectMapper.readValue(list,
                typeFactory.constructCollectionType(List.class, CadStage.class));

            List<CadStage> makerList = mapPreviousList.stream()
                .filter(a -> a.getFromRole().getRoleType().equals(RoleType.MAKER) || a.getToRole()
                    .getRoleType().equals(RoleType.MAKER))
                .collect(Collectors.toList());
            Optional<CadStage> latestMaker = makerList.stream()
                .max(Comparator.comparing(CadStage::getLastModifiedAt));
            Long rId = latestMaker.get().getFromRole().getRoleType().equals(RoleType.MAKER)
                ? latestMaker.get().getFromRole().getId()
                : latestMaker.get().getToRole().getId();
            final List<User> users = userService
                .findByRoleAndBranchId(rId, branchID);
            final List<User> activeUser = users.stream()
                .filter(f -> f.getStatus().equals(Status.ACTIVE) && f.getRole().getRoleType()
                    .equals(RoleType.MAKER)).collect(
                    Collectors.toList());
            final List<Long> userIdList = activeUser.stream().map(User::getId)
                .collect(Collectors.toList());
            Long uId = latestMaker.get().getFromRole().getRoleType().equals(RoleType.MAKER)
                ? latestMaker.get().getFromUser().getId()
                : latestMaker.get().getToUser().getId();
            if (userIdList.contains(uId)) {
                map.put(USER_ID, uId);
                map.put(ROLE_ID, rId);
            } else {
                map.put(USER_ID, activeUser.get(0).getId());
                map.put(ROLE_ID, activeUser.get(0).getRole().getId());
            }


        } catch (Exception e) {
            log.error(
                "unable to get users for backward::getCADMaker searching for Loan Maker ..... ");

        }
        return map;
    }

    private Map<String, Long> getCADUser(List<CadStage> list, Long branchID) {
        Map<String, Long> map = new HashMap<>();
        try {
            List<CadStage> mapPreviousList = list;
            List<CadStage> cadUserList = mapPreviousList.stream()
                .filter(a -> a.getFromRole().getRoleName().equalsIgnoreCase("CAD")).collect(
                    Collectors.toList());
            if (cadUserList.isEmpty() || ObjectUtils.isEmpty(cadUserList)) {
                List<UserDto> userByRoleCad = userService.getUserByRoleCad();
                map.put(USER_ID, userByRoleCad.get(0).getId());
                map.put(ROLE_ID, userByRoleCad.get(0).getRole().getId());
            } else {
                map.put(USER_ID, cadUserList.get(0).getFromUser().getId());
                map.put(ROLE_ID, cadUserList.get(0).getFromRole().getId());
            }


        } catch (Exception e) {
            log.error("unable to get users cad User ", e);

        }
        return map;
    }


}
