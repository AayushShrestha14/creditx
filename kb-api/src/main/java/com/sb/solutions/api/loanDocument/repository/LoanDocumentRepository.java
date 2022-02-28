package com.sb.solutions.api.loanDocument.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.loanDocument.entity.LoanDocument;

@Repository
public interface LoanDocumentRepository extends JpaRepository<LoanDocument, Long> {

}
