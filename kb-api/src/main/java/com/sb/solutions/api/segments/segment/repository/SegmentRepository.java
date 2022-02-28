package com.sb.solutions.api.segments.segment.repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.segments.segment.entity.Segment;

public interface SegmentRepository extends JpaRepository<Segment, Long> {

    @Query(value = "select"
        + "  (select  count(id) from Segment where status=1) active,"
        + "(select  count(id) from Segment where status=0) inactive,"
        + "(select  count(id) from Segment) segments", nativeQuery = true)
    Map<Object, Object> segmentStatusCount();

    @Query(value = "select s from Segment s where s.segmentName like concat(:segmentName,'%')")
    Page<Segment> segmentFilter(@Param("segmentName") String segmentName, Pageable pageable);
}
