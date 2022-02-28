package com.sb.solutions.api.segments.subSegment.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.segments.subSegment.entity.SubSegment;

public interface SubSegmentRepository extends JpaRepository<SubSegment, Long> {

    @Query(value = "select"
        + "  (select  count(id) from Sub_Segment where status=1) active,"
        + "(select  count(id) from Sub_Segment where status=0) inactive,"
        + "(select  count(id) from Sub_Segment) subSegments", nativeQuery = true)
    Map<Object, Object> subSegmentStatusCount();

    @Query(value = "select s from SubSegment s"
        + " where s.subSegmentName like concat(:subSegmentName,'%')")
    Page<SubSegment> subSegmentFilter(@Param("subSegmentName") String subSegmentName,
        Pageable pageable);
}
