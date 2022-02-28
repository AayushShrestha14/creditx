package com.sb.solutions.api.fiscalyear.repository;

import com.sb.solutions.api.fiscalyear.entity.FiscalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalYearRepository extends JpaRepository<FiscalYear, Long> {

    @Modifying
    @Query("update FiscalYear set isCurrentYear = false ")
    void deActivePreviousFiscalYear();

    List<FiscalYear> findAllByOrderByIsCurrentYearDesc();

    List<FiscalYear> findAllByOrderByYear();
}
