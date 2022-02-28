package com.sb.solutions.api.guarantor.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.guarantor.repository.GuarantorRepository;

@Service
public class GuarantorServiceImpl implements GuarantorService {

    @Autowired
    GuarantorRepository guarantorRepository;

    @Override
    public List<Guarantor> findAll() {
        return guarantorRepository.findAll();
    }

    @Override
    public Guarantor findOne(Long id) {
        return guarantorRepository.getOne(id);
    }

    @Override
    public Guarantor save(Guarantor guarantor) {
        guarantor.setLastModifiedAt(new Date());
        return guarantorRepository.save(guarantor);
    }

    @Override
    public Page<Guarantor> findAllPageable(Object object, Pageable pageable) {
        return null;
    }

    @Override
    public List<Guarantor> saveAll(List<Guarantor> list) {
        return guarantorRepository.saveAll(list);
    }
}
