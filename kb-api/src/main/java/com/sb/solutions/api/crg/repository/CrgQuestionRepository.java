package com.sb.solutions.api.crg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.core.enums.Status;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */

public interface CrgQuestionRepository extends JpaRepository<CrgQuestion, Long> {

    List<CrgQuestion> findByLoanConfigIdAndStatusNotOrderByCrgGroupIdAsc(
            Long loanConfigId, Status status);

}
