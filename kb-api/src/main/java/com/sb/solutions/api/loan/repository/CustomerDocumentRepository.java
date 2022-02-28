package com.sb.solutions.api.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.loan.entity.CustomerDocument;

/**
 * @author yunish on 11/5/2019
 */
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {

}
