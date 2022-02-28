package com.sb.solutions.web.eligibility.v1.applicant.dto;

import lombok.Data;

import com.sb.solutions.core.enums.Status;

@Data
public class AnswerDto {

    private String description;

    private long points;

    private Status status;

    private QuestionDto question;
}
