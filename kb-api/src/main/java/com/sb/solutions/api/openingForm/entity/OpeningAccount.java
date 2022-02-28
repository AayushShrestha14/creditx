package com.sb.solutions.api.openingForm.entity;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningAccount {

    private AccountCategory purposeOfAccount;
    private String currency;
    private boolean haveJoint;
    private boolean haveNominee;
    private boolean haveBeneficiary;
    private OpeningNominee nominee;
    private OpeningBeneficiary beneficiary;
    private Set<OpeningCustomer> openingCustomers;
    private String annualTransactionNumber;
    private String annualTransaction;
    private boolean internetBanking;
    private boolean mobileBanking;
    private boolean debitCard;
    private boolean statement;
    private String statementFrequency;
    private String statementMode;

}
