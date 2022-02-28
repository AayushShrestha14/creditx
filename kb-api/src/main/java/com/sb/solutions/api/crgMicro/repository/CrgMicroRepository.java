package com.sb.solutions.api.crgMicro.repository;

import com.sb.solutions.api.crgMicro.entity.CrgMicro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Amulya Shrestha on 5/23/2021
 **/

@Repository
public interface CrgMicroRepository extends JpaRepository<CrgMicro, Long> {
}
