package com.sb.solutions.api.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Elvin Shrestha on 9/10/2020
 */
@Data
@AllArgsConstructor
public class HelperDto<I> {

    private I id;
    private HelperIdType idType;
}
