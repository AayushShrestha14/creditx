package com.sb.solutions.api.address.district.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.province.entity.Province;

public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query(value = "select b from District b where b.name like  concat(:name,'%')")
    Page<District> districtFilter(@Param("name") String name, Pageable pageable);

    List<District> findAllByProvince(Province province);
}
