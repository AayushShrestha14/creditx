package com.sb.solutions.core.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApimsDto {

    private String FunctionName;
    private String Data;
    private String Signature;
    private String TimeStamp;
}
