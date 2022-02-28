package com.sb.solutions.api.segments.segment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.segments.segment.entity.Segment;
import com.sb.solutions.api.segments.segment.repository.SegmentRepository;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SegmentServiceImpl implements SegmentService {

    private final SegmentRepository segmentRepository;

    @Override
    public List<Segment> findAll() {
        return segmentRepository.findAll();
    }

    @Override
    public Segment findOne(Long id) {
        return segmentRepository.getOne(id);
    }

    @Override
    public Segment save(Segment segment) {
        segment.setLastModifiedAt(new Date());
        if (segment.getId() == null) {
            segment.setStatus(Status.ACTIVE);
        }
        return segmentRepository.save(segment);
    }

    @Override
    public Page<Segment> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return segmentRepository.segmentFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<Segment> saveAll(List<Segment> list) {
        return segmentRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> segmentStatusCount() {
        return segmentRepository.segmentStatusCount();
    }
}
