package com.sb.solutions.api.address.province.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.address.province.entity.Province;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query(value = "select b from Province b where b.name like  concat(:name,'%')")
    Page<Province> provinceFilter(@Param("name") String name, Pageable pageable);
}
