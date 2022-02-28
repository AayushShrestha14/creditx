package com.sb.solutions.api.cbsGroup.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.cbsGroup.repository.CbsRepository;

/**
 * @author : Rujan Maharjan on  12/20/2020
 **/
@Service
public class GetCbsDataServiceImpl implements GetCbsDataService {

    private final CbsRepository cbsRepository;

    public GetCbsDataServiceImpl(CbsRepository cbsRepository) {
        this.cbsRepository = cbsRepository;
    }

    @Override
    public List<Map<String,Object>> getAllData() {
        return cbsRepository.getAllData();
    }

}
