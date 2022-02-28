package com.sb.solutions.api.guarantor.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.guarantor.entity.GuarantorDetail;
import com.sb.solutions.api.guarantor.repository.GuarantorDetailRepository;

/**
 * @author Elvin Shrestha on 2/27/2020
 */
@Service
public class GuarantorDetailServiceImpl implements GuarantorDetailService {

    private final GuarantorDetailRepository repository;

    public GuarantorDetailServiceImpl(
        GuarantorDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GuarantorDetail> findAll() {
        return repository.findAll();
    }

    @Override
    public GuarantorDetail findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public GuarantorDetail save(GuarantorDetail guarantorDetail) {
        return repository.save(guarantorDetail);
    }

    @Override
    public Page<GuarantorDetail> findAllPageable(Object t, Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<GuarantorDetail> saveAll(List<GuarantorDetail> list) {
        return repository.saveAll(list);
    }
}
