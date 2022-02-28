package com.sb.solutions.api.nepseCompany.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.repository.BaseRepository;

public interface NepseCompanyRepository extends BaseRepository<NepseCompany, Long> {

    @Query(value = "select n from NepseCompany n where n.companyName like  concat(:companyName,'%')"
        + " order by n.status desc ")
    Page<NepseCompany> nepseCompanyFilter(@Param("companyName") String companyName,
        Pageable pageable);

    @Query(value = "select "
        + "  (select  count(id) from Nepse_Company where status=1) active,"
        + "(select  count(id) from Nepse_Company where status=0) inactive,"
        + "(select  count(id) from Nepse_Company) nepses", nativeQuery = true)
    Map<Object, Object> nepseCompanyStatusCount();

    List<NepseCompany> findByStatus(Status status);
}
