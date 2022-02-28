package com.sb.solutions.api.document.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.document.entity.LoanCycle;
import com.sb.solutions.api.document.repository.LoanCycleRepository;
import com.sb.solutions.core.dto.SearchDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class LoanCycleServiceImpl implements LoanCycleService {

    private final LoanCycleRepository loanCycleRepository;

    @Override
    public List<LoanCycle> findAll() {
        return loanCycleRepository.findAll();
    }

    @Override
    public LoanCycle findOne(Long id) {
        try {
            return loanCycleRepository.findById(id).get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LoanCycle save(LoanCycle loanCycle) {
        return loanCycleRepository.save(loanCycle);
    }


    @Override
    public Page<LoanCycle> findAllPageable(Object loanCycle, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(loanCycle, SearchDto.class);
        return loanCycleRepository
            .loanCycleFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<LoanCycle> saveAll(List<LoanCycle> list) {
        return loanCycleRepository.saveAll(list);
    }


}
