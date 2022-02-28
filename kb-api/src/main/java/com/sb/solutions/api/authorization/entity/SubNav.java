package com.sb.solutions.api.authorization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubNav {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String subNavName;
    private String frontUrl;
    private String faIcon;


}
