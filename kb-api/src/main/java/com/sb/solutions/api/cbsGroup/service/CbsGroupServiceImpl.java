package com.sb.solutions.api.cbsGroup.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.cbsGroup.config.DataSourceConfig;
import com.sb.solutions.api.cbsGroup.entity.CbsGroup;
import com.sb.solutions.api.cbsGroup.repository.CbsGroupRepository;
import com.sb.solutions.api.cbsGroup.repository.specification.CbsGroupSpecBuilder;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.utils.ProductUtils;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/

@Service
@Slf4j
public class CbsGroupServiceImpl implements CbsGroupService {

    private final CbsGroupRepository cbsGroupRepository;
    private final GetCbsDataService getCbsDataService;
    private final DataSourceConfig dataSourceConfig;

    private ObjectMapper mapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));

    public CbsGroupServiceImpl(
        CbsGroupRepository cbsGroupRepository,
        GetCbsDataService getCbsDataService,
        DataSourceConfig dataSourceConfig) {
        this.cbsGroupRepository = cbsGroupRepository;
        this.getCbsDataService = getCbsDataService;
        this.dataSourceConfig = dataSourceConfig;
    }

    @Override
    public List<CbsGroup> findAll() {
        return cbsGroupRepository.findAll();
    }

    @Override
    public CbsGroup findOne(Long id) {
        return cbsGroupRepository.findById(id).get();
    }

    @Override
    public CbsGroup save(CbsGroup cbsGroup) {
        return cbsGroupRepository.save(cbsGroup);
    }

    @Override
    public Page<CbsGroup> findAllPageable(Object t, Pageable pageable) {
        Map<String, String> s = mapper.convertValue(t, Map.class);
        s.values().removeIf(Objects::isNull);
        final CbsGroupSpecBuilder cbsGroupSpecBuilder = new CbsGroupSpecBuilder(s);
        Specification<CbsGroup> specification = cbsGroupSpecBuilder.build();
        return cbsGroupRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public List<CbsGroup> saveAll(List<CbsGroup> list) {
        String OBLIGOR_KEY = this.dataSourceConfig.getUniqueKeyForFilter();
        List<Map<String, Object>> cbsRemoteData = getCbsDataService.getAllData();

        //if only to save data with obl nt null
        List<Map<String, Object>> cbsRemoteDataWithOblNotNull = cbsRemoteData.stream().filter(
            f -> f.containsKey(OBLIGOR_KEY) && !ObjectUtils.isEmpty(f.get(OBLIGOR_KEY))
        ).collect(Collectors.toList());
        List<CbsGroup> cbsGroupList = cbsRemoteData.stream().map(m -> {
            CbsGroup cbsGroup = new CbsGroup();
            cbsGroup.setObligor(ObjectUtils.isEmpty(m.get(OBLIGOR_KEY)) ? null
                : m.get(OBLIGOR_KEY).toString().trim());
            try {
                cbsGroup.setJsonData(mapper.writeValueAsString(m));
            } catch (JsonProcessingException e) {
                log.error("unable to parse json cbs data");
            }
            return cbsGroup;
        }).collect(Collectors.toList());
        if (!cbsRemoteData.isEmpty()) {
            cbsGroupRepository.deleteAllInBatch();
            return cbsGroupRepository.saveAll(cbsGroupList);
        } else {
            log.error("no data present in remote db");
            return null;
        }

    }

    @Override
    public List<CbsGroup> findCbsGroupByObl(String obligor) {
        return cbsGroupRepository.findCbsGroupByObligor(obligor);
    }

    @Scheduled(cron = "0 0 20 * * ?")
    public void fetchData() {
        if (ProductUtils.CBS_ENABLE) {
            this.saveAll(new ArrayList<>());
        }
    }
}
