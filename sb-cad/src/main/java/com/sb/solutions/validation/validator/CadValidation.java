package com.sb.solutions.validation.validator;

import com.sb.solutions.constant.ErrorMessage;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.exception.handler.Violation;
import com.sb.solutions.dto.CadStageDto;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Rujan Maharjan on  12/4/2020
 **/
public class CadValidation {

    private CadStageDto t = new CadStageDto();

    private List<Violation> violations = new ArrayList<>();

    private void toUserValidation() {
        if (ObjectUtils.isEmpty(t.getToUser())) {
            violations.add(new Violation("toUser", null, ErrorMessage.EMPTY_USER));

        }
        if ((!ObjectUtils.isEmpty(t.getToUser())) && ObjectUtils.isEmpty(t.getToUser().getId())) {
            violations.add(new Violation("toUser", null, ErrorMessage.EMPTY_USER));
        }
    }

    private void toRoleValidation() {
        if (ObjectUtils.isEmpty(t.getToRole())) {
            violations.add(new Violation("toRole", null, ErrorMessage.EMPTY_ROLE));
        }
        if ((!ObjectUtils.isEmpty(t.getToRole())) && ObjectUtils.isEmpty(t.getToRole().getId())) {
            violations.add(new Violation("toRole", null, ErrorMessage.EMPTY_ROLE));
        }
    }

    private void commentValidation() {
        if (ObjectUtils.isEmpty(t.getComment())) {
            violations.add(new Violation("comment", null, ErrorMessage.EMPTY_COMMENT));
        }
    }

    private void loanListValidation() {
        if (ObjectUtils.isEmpty(t.getCustomerLoanDtoList())) {
            violations.add(new Violation("loan", null, ErrorMessage.ERROR_EMPTY_LIST));
        } else if (t.getCustomerLoanDtoList().size() == 0) {
            violations.add(new Violation("loan", null, ErrorMessage.ERROR_EMPTY_LIST));
        }
    }

    public void validateBy(boolean toUser, boolean toRole, boolean comment,boolean loan, CadStageDto cadStageDto) {
        this.t = cadStageDto;
        this.violations = new ArrayList<>();
        if (toRole) {
            this.toRoleValidation();
        }
        if (toUser) {
            this.toUserValidation();
        }
        if (comment) {
            this.commentValidation();
        }
        if (loan) {
            this.loanListValidation();
        }
        if (!this.violations.isEmpty()) {
            throw new ServiceValidationException("Invalid Request", this.violations);
        }
    }
}
