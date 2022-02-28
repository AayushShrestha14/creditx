package com.sb.solutions.api.crg.repository;

import com.sb.solutions.api.crg.entity.CrgAnswer;
import com.sb.solutions.core.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrgAnswerRepository extends JpaRepository<CrgAnswer, Long> {

    List<CrgAnswer> findAllByCrgQuestionIdAndStatusNot(long questionId, Status status);

    List<CrgAnswer> findByIdIn(List<Long> ids);

}
