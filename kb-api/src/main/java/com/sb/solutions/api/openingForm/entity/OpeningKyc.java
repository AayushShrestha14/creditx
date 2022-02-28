package com.sb.solutions.api.openingForm.entity;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningKyc {

    private Set<OpeningOccupationalDetails> occupationalDetails;
    private Set<OpeningCustomerRelative> customerRelatives;
}
