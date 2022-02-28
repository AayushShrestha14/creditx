package com.sb.solutions.api.preference.blacklist.repository;

import java.util.List;

import com.sb.solutions.api.preference.blacklist.entity.BlackList;
import com.sb.solutions.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BlackListRepository extends BaseRepository<BlackList, Long> {
    List<BlackList> findBlackListByRef(String ref);

    @Query(value = "select n from BlackList n where n.name like  concat(:name,'%')"
            + " order by n.name asc ")
    Page<BlackList> blackListFilter(@Param("name") String name,
                                          Pageable pageable);
}
