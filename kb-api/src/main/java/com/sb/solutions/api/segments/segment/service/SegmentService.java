package com.sb.solutions.api.segments.segment.service;

import java.util.Map;

import com.sb.solutions.api.segments.segment.entity.Segment;
import com.sb.solutions.core.service.BaseService;

public interface SegmentService extends BaseService<Segment> {

    Map<Object, Object> segmentStatusCount();
}
