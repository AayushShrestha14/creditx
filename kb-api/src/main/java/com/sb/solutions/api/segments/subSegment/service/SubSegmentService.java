package com.sb.solutions.api.segments.subSegment.service;

import java.util.Map;

import com.sb.solutions.api.segments.subSegment.entity.SubSegment;
import com.sb.solutions.core.service.BaseService;

public interface SubSegmentService extends BaseService<SubSegment> {

    Map<Object, Object> subSegmentStatusCount();
}
