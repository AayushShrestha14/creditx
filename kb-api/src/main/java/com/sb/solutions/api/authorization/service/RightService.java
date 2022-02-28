package com.sb.solutions.api.authorization.service;

import java.util.List;

import com.sb.solutions.api.authorization.entity.Rights;

/**
 * @author Rujan Maharjan on 3/31/2019
 */
public interface RightService {

    List<Rights> getAll();
}
