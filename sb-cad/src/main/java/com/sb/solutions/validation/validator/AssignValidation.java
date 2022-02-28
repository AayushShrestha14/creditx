package com.sb.solutions.validation.validator;

import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.constant.ErrorMessage;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.exception.handler.Violation;
import com.sb.solutions.dto.CadStageDto;
import com.sb.solutions.validation.constraint.CadValid;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Rujan Maharjan on  12/4/2020
 **/
@Aspect
@Component
@Slf4j
public class AssignValidation {

    private final UserService userService;


    public AssignValidation(UserService userService) {
        this.userService = userService;
    }

    @Pointcut("execution(* com.sb.solutions..*.*(..))")
    public void serviceLayer() {
    }

    @Before(value = "serviceLayer() && @annotation(cadValid) && args(batch)")
    public void validateBeforeAssigningDocument(JoinPoint pjp, CadStageDto batch,
                                                CadValid cadValid) {
        List<Violation> violations = new ArrayList<>();
        CadValidation cadValidation = new CadValidation();
        cadValidation.validateBy(true, true, false, true, batch);
        final User toUser = userService.findOne(batch.getToUser().getId());

        if (ObjectUtils.isEmpty(toUser)) {
            violations.add(new Violation("toUser", null, ErrorMessage.EMPTY_USER));
            throw new ServiceValidationException("invalid request", violations);
        }

        if (!toUser.getStatus().equals(Status.ACTIVE)) {
            violations.add(new Violation("toUser", null, ErrorMessage.INACTIVE_USER));
            throw new ServiceValidationException("invalid request", violations);
        }

    }


}
