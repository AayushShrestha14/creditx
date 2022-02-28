package com.sb.solutions.web.navigation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 5/17/2019
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {

    private String title;
    private String link;
    private String icon;
    private List<MenuDto> children;

}
