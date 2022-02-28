package com.sb.solutions.api.address.municipalityVdc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;

public interface MunicipalityVdcRepository extends JpaRepository<MunicipalityVdc, Long> {

    @Query(value = "select b from MunicipalityVdc b where b.name like  concat(:name,'%')")
    Page<MunicipalityVdc> municipalityVdcFilter(@Param("name") String name, Pageable pageable);

    List<MunicipalityVdc> findAllByDistrict(District district);
}
