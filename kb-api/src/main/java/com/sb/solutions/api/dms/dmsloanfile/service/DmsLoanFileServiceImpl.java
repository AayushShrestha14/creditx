package com.sb.solutions.api.dms.dmsloanfile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;
import com.sb.solutions.api.dms.dmsloanfile.repository.DmsLoanFileRepository;
import com.sb.solutions.api.dms.dmsloanfile.repository.specification.DmsSpecBuilder;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.exception.handler.Violation;
import com.sb.solutions.core.utils.date.DateValidation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DmsLoanFileServiceImpl implements DmsLoanFileService {

    DmsLoanFileRepository dmsLoanFileRepository;
    private Gson gson;
    private static final Logger logger = LoggerFactory.getLogger(DmsLoanFileServiceImpl.class);

    @Override
    public List<DmsLoanFile> findAll() {
        return dmsLoanFileRepository.findAll();
    }

    @Override
    public DmsLoanFile findOne(Long id) {
        final DmsLoanFile d = dmsLoanFileRepository.getOne(id);
        return d;
    }

    @Override
    public DmsLoanFile save(DmsLoanFile dmsLoanFile) {
        if (dmsLoanFile.getTenure() != null && !DateValidation
            .isFutureDate(dmsLoanFile.getTenure())) {
            final Violation violation = new Violation("tenure", dmsLoanFile.getTenure(),
                "Invalid tenure date");

            throw new ServiceValidationException("Invalid dms Loan",
                Lists.newArrayList(violation));
        }
        logger.debug("docs {}", dmsLoanFile.getDocumentMap());
        dmsLoanFile.setDocumentPath(gson.toJson(dmsLoanFile.getDocumentMap()));
        dmsLoanFile.setCreatedAt(new Date());
        return dmsLoanFileRepository.save(dmsLoanFile);
    }

    @Override
    public Page<DmsLoanFile> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(object, Map.class);
        final DmsSpecBuilder dmsSpecBuilder = new DmsSpecBuilder(s);
        final Specification<DmsLoanFile> specification = dmsSpecBuilder.build();
        return dmsLoanFileRepository.findAll(specification, pageable);
    }

    @Override
    public List<DmsLoanFile> saveAll(List<DmsLoanFile> list) {
        return dmsLoanFileRepository.saveAll(list);
    }
}
