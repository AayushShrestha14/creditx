package com.sb.solutions.api.preference.blacklist.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sb.solutions.api.preference.blacklist.entity.BlackList;
import com.sb.solutions.core.service.BaseService;

public interface BlackListService extends BaseService<BlackList> {
    void saveList(List<BlackList> blackList);

    Page<BlackList> findAllBlackList(Pageable pageable);

    void removeById(Long id);

    boolean checkBlackListByRef(String ref);
}
