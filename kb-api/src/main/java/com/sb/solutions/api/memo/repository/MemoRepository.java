package com.sb.solutions.api.memo.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.core.repository.BaseRepository;

@Repository
public interface MemoRepository extends BaseRepository<Memo, Long> {

}
