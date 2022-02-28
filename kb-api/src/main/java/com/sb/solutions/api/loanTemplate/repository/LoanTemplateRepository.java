package com.sb.solutions.api.loanTemplate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.loanTemplate.entity.LoanTemplate;

/**
 * @author Rujan Maharjan on 2/25/2019
 */
public interface LoanTemplateRepository extends JpaRepository<LoanTemplate, Long> {

    @Query(value = "select l from LoanTemplate l where l.name like  concat(:name,'%')")
    Page<LoanTemplate> loanTemplateFilter(@Param("name") String name, Pageable pageable);
}
