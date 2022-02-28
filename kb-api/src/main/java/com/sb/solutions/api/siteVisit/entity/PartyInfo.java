package com.sb.solutions.api.siteVisit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyInfo {

    private String party;

    private double threeMonth;

    private double sixMonth;

    private double oneYear;

    private double moreThanOneYear;
}
