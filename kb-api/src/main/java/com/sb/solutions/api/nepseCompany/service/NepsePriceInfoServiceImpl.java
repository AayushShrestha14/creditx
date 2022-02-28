package com.sb.solutions.api.nepseCompany.service;

import com.sb.solutions.api.nepseCompany.entity.NepsePriceInfo;
import com.sb.solutions.api.nepseCompany.repository.NepsePriceInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NepsePriceInfoServiceImpl implements NepsePriceInfoService {
   private NepsePriceInfoRepository nepsePriceInfoRepository;

    public NepsePriceInfoServiceImpl(NepsePriceInfoRepository nepsePriceInfoRepository) {
        this.nepsePriceInfoRepository = nepsePriceInfoRepository;
    }

    @Override
    public List<NepsePriceInfo> findAll() {
        return nepsePriceInfoRepository.findAll();
    }

    @Override
    public NepsePriceInfo findOne(Long id) {
        return nepsePriceInfoRepository.getOne(id);
    }

    @Override
    public NepsePriceInfo save(NepsePriceInfo nepsePriceInfo) {
        return nepsePriceInfoRepository.save(nepsePriceInfo);
    }

    @Override
    public Page<NepsePriceInfo> findAllPageable(Object t, Pageable pageable) {
        return nepsePriceInfoRepository.findAll(pageable);
    }

    @Override
    public List<NepsePriceInfo> saveAll(List<NepsePriceInfo> list) {
        return nepsePriceInfoRepository.saveAll(list);
    }

    @Override
    public NepsePriceInfo getActivePriceInfo() {
        return nepsePriceInfoRepository.findFirstByOrderByIdDesc();
    }

    @Override
    public void deletePrevRecord() {
        nepsePriceInfoRepository.deleteAll();
    }
}
