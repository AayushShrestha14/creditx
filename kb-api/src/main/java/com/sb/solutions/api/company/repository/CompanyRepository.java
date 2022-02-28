package com.sb.solutions.api.company.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.company.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query(value = "SELECT "
        + "(SELECT  count(id) FROM Company WHERE status=1) active,"
        + "(SELECT  count(id) FROM Company WHERE status=0) inactive,"
        + "(SELECT  count(id) FROM Company) companys", nativeQuery = true)
    Map<Object, Object> companyStatusCount();

    @Query(value = "SELECT c FROM Company c WHERE c.name LIKE CONCAT(:companyName,'%')")
    Page<Company> companyFilter(@Param("companyName") String companyName, Pageable pageable);
}
