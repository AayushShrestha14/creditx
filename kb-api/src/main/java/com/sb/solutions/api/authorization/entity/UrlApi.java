package com.sb.solutions.api.authorization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 4/19/2019
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String apiUrl;
    @NotNull
    private String type;
    @Transient
    private boolean checked;

}
