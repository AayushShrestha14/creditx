package com.sb.solutions.api.authorization.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enums.Status;

/**
 * Created by Rujan Maharjan on 3/25/2019.
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private String permissionName;
    private String frontUrl;
    private String faIcon;
    private Long orders;
    private Status status = Status.ACTIVE;

    private Boolean visible=true;

    @OneToMany
    private List<SubNav> subNavs;

    @OneToMany
    private List<UrlApi> apiList;
}

