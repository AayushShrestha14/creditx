package com.sb.solutions.api.memo.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.core.repository.BaseSpecBuilder;

public class MemoSpecBuilder extends BaseSpecBuilder<Memo> {

    public MemoSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<Memo> getSpecification(String property, String filterValue) {
        return new MemoSpec(property, filterValue);
    }
}
