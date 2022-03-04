package com.sb.solutions.web.accountOpening.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadDto {

    private byte[] multipartFile;
    private String originalFilename;
    private String type;
    private String name;
    private String citizenship;
    private String branch;
    private String customerName;
}
