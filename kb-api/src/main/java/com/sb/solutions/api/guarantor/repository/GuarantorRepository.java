package com.sb.solutions.api.guarantor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.guarantor.entity.Guarantor;

@Repository
public interface GuarantorRepository extends JpaRepository<Guarantor, Long> {

}
