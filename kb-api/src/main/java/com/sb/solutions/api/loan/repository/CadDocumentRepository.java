package com.sb.solutions.api.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.loan.entity.CadDocument;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import javax.transaction.Transactional;

public interface CadDocumentRepository extends JpaRepository<CadDocument, Long> {

    @Modifying
    @Transactional
    @Query(value = "delete from customer_loan_cad_document where customer_loan_id=? AND cad_document_id=?", nativeQuery = true)
    int deleteByLoanIdAndDocument(Long customerLoanId, Long docId);

}
