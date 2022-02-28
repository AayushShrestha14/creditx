package com.sb.solutions.api.mGroupInfo.repository;

import com.sb.solutions.api.mGroupInfo.entity.MGroupInfo;
import com.sb.solutions.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MGroupRepo extends BaseRepository<MGroupInfo , Long> {
}
