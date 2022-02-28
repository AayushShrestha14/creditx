package com.sb.solutions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDto {

    private byte[] multipartFile;
    private String fileName;
    private String type;
    private String citizenship;
    private String branch;
    private String customerName;
}