package com.sb.solutions.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignatureDto {

    @JsonProperty("Model")
    private Object Model;

    @JsonProperty("TimeStamp")
    private String TimeStamp;
}
