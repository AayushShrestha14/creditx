package com.sb.solutions.api.cicl.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.cicl.entity.Cicl;
import com.sb.solutions.api.cicl.repository.CiclRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author : Rujan Maharjan on  9/30/2020
 **/
@Service
public class CiclServiceImpl extends BaseServiceImpl<Cicl, Long> implements CIclService {

    private CiclRepository ciclRepository;

    protected CiclServiceImpl(
        CiclRepository ciclRepository) {
        super(ciclRepository);
    }

    @Override
    protected BaseSpecBuilder<Cicl> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
