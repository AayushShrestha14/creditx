package com.sb.solutions.api.memo.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;

public interface MemoTypeService extends BaseService<MemoType> {

    List<MemoType> findByStatus(Status status);

    List<MemoType> saveAll(List<MemoType> types);

    void delete(MemoType type);

    void deleteById(long id);

    Page<MemoType> findPageable(Map<String, String> filterParams, Pageable pageable);

    Page<MemoType> findPageable(Pageable pageable);
}
