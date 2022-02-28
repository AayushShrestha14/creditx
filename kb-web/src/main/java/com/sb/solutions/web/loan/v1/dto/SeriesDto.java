package com.sb.solutions.web.loan.v1.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SeriesDto {

    private String name;
    private BigDecimal value;
    private Long fileCount;
}
