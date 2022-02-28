package com.sb.solutions.api.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.authorization.entity.Rights;

/**
 * @author Rujan Maharjan on 3/31/2019
 */
public interface RightRepository extends JpaRepository<Rights, Long> {

}
