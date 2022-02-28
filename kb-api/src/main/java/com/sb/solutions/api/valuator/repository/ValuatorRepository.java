package com.sb.solutions.api.valuator.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.valuator.entity.Valuator;

public interface ValuatorRepository extends JpaRepository<Valuator, Long>,
    JpaSpecificationExecutor<Valuator> {

    @Query(value = "select "
        + "  (select  count(id) from valuator where status=1) active,"
        + "(select  count(id) from valuator where status=0) inactive,"
        + "(select  count(id) from valuator) valuators", nativeQuery = true)
    Map<Object, Object> valuatorStatusCount();

    @Query(value = "select v from Valuator v where v.name like concat(:name,'%')")
    Page<Valuator> valuatorFilter(@Param("name") String name, Pageable pageable);



}
