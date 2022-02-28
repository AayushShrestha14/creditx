package com.sb.solutions.api.loanTemplate.service;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.loanTemplate.entity.LoanTemplate;
import com.sb.solutions.api.loanTemplate.repository.LoanTemplateRepository;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 2/25/2019
 */
@Service
public class LoanTemplateServiceImpl implements LoanTemplateService {

    @Autowired
    LoanTemplateRepository loanTemplateRepository;

    @Override
    public List<LoanTemplate> findAll() {
        return loanTemplateRepository.findAll();
    }

    @Override
    public LoanTemplate findOne(Long id) {
        return loanTemplateRepository.findById(id).get();
    }

    @Override
    public LoanTemplate save(LoanTemplate loanTemplate) {
        loanTemplate.setLastModifiedAt(new Date());
        if (loanTemplate.getId() == null) {
            loanTemplate.setStatus(Status.ACTIVE);

        }
        return loanTemplateRepository.save(loanTemplate);
    }


    @Override
    public Page<LoanTemplate> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return loanTemplateRepository
            .loanTemplateFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<LoanTemplate> saveAll(List<LoanTemplate> list) {
        return loanTemplateRepository.saveAll(list);
    }
}
