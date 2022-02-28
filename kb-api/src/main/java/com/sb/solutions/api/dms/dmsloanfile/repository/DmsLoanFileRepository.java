package com.sb.solutions.api.dms.dmsloanfile.repository;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;

@Repository
public interface DmsLoanFileRepository extends JpaRepository<DmsLoanFile, Long>,
    JpaSpecificationExecutor<DmsLoanFile> {

    @Query(value = "select "
        + "(select  count(id) from dms_loan_file) pendings", nativeQuery = true)
    Map<Object, Object> pendingStatusCount();
}
