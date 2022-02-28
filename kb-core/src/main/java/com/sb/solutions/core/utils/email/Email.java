package com.sb.solutions.core.utils.email;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.utils.email.dto.InsuranceEmailDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private List<String> cc;
    private List<String> bcc;
    private String body;
    private List<String> attachment;
    private String subject;
    private String to;
    private String toName;
    private String bankName;
    private String bankBranch;
    private String bankWebsite;
    private String accountType;
    private String resetPasswordLink;
    private String expiry;
    private String loanType;
    private String name;
    private String email;
    private String phoneNumber;
    private LoanType loanTypes;
    private String clientCitizenshipNumber;
    private List<InsuranceEmailDto> insurances;
    private String accountNumber;
    private String affiliateId;

}
