package com.sb.solutions.api.memo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.enums.Status;

@Repository
public interface MemoTypeRepository extends JpaRepository<MemoType, Long>,
    JpaSpecificationExecutor<MemoType> {

    List<MemoType> findByStatus(Status status);
}
