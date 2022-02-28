package com.sb.solutions.api.loan;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 7/15/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PieChartDto {

    private String name;
    private BigDecimal value;

}
