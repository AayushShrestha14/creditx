package com.sb.solutions.api.eligibility.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.eligibility.document.entity.SubmissionDocument;

@Repository
public interface SubmissionDocumentRepository extends JpaRepository<SubmissionDocument, Long> {

}
