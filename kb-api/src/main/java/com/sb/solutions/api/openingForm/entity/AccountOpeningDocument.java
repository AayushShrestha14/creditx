package com.sb.solutions.api.openingForm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Elvin Shrestha on 6/9/2020
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOpeningDocument {
    String path;
    Long documentId;
}
