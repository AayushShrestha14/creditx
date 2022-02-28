package com.sb.solutions.core.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 2/25/2019
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {

    private String name;
    private Long provinceId;
    private Long districtId;
    private Long municipalityId;
    private Date date;
    private String loanType;
    private String citizenship;
    private Status status;
}
