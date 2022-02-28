package com.sb.solutions.api.creditChecklist.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.creditChecklist.entity.CreditChecklist;
import com.sb.solutions.api.creditChecklist.repository.CreditChecklistRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

@Service
public class CreditChecklistServiceImpl extends BaseServiceImpl<CreditChecklist, Long> implements
    CreditChecklistService {

    private CreditChecklistRepository creditChecklistRepository;

    protected CreditChecklistServiceImpl(
        CreditChecklistRepository checklistRepository) {
        super(checklistRepository);
        this.creditChecklistRepository = checklistRepository;
    }

    @Override
    protected BaseSpecBuilder<CreditChecklist> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
