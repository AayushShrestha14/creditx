package com.sb.solutions.api.mGroupInfo.service;

import com.sb.solutions.api.mGroupInfo.entity.MGroupInfo;
import com.sb.solutions.api.mGroupInfo.repository.MGroupRepo;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MGroupServiceImpl extends BaseServiceImpl<MGroupInfo , Long> implements MGroupService {
    protected MGroupServiceImpl(MGroupRepo repository) {
        super(repository);
    }

    @Override
    protected BaseSpecBuilder<MGroupInfo> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
