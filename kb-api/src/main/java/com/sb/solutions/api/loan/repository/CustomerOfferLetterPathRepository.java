package com.sb.solutions.api.loan.repository;

import com.sb.solutions.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.loan.entity.CustomerOfferLetterPath;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerOfferLetterPathRepository extends JpaRepository<CustomerOfferLetterPath, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE CustomerOfferLetterPath c set c.isApproved = :isApproved ,c.approvedBy = :approvedBy WHERE c.id in (:ids)")
    void updateApproveStatusAndApproveBy(@Param("isApproved") Boolean isApproved, @Param("approvedBy") User approvedBy, @Param("ids") List<Long> id);
}
