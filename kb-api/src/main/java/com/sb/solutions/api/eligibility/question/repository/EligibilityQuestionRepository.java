package com.sb.solutions.api.eligibility.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.eligibility.question.entity.EligibilityQuestion;

@Repository
public interface EligibilityQuestionRepository extends JpaRepository<EligibilityQuestion, Long> {

}
