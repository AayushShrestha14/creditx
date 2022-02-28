package com.sb.solutions.api.memo.repository;

import org.springframework.stereotype.Component;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.revision.RevisionRepository;

@Component
public class MemoTypeRevisionRepository extends RevisionRepository<MemoType> {

    @Override
    public Class<MemoType> getClazz() {
        return MemoType.class;
    }
}
