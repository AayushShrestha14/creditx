package com.sb.solutions.web.eligibility.v1.applicant.dto;

import lombok.Data;

@Data
public class EligibilityAnswerDto {

    private EligibilityQuestionDto eligibilityQuestion;

    private double value;
}
