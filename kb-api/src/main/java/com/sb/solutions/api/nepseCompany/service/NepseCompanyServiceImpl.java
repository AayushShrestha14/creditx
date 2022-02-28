package com.sb.solutions.api.nepseCompany.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.loan.service.CustomerShareLoanThreadService;
import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.api.nepseCompany.entity.NepsePriceInfo;
import com.sb.solutions.api.nepseCompany.repository.NepseCompanyRepository;
import com.sb.solutions.api.nepseCompany.repository.specification.NepseSpecBuilder;
import com.sb.solutions.api.nepseCompany.util.NepseExcelReader;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.FilterJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class NepseCompanyServiceImpl implements NepseCompanyService {

    private final Logger logger = LoggerFactory.getLogger(NepseCompanyServiceImpl.class);

    private final NepseCompanyRepository nepseCompanyRepository;
    private final CustomerShareLoanThreadService customerShareLoanThreadService;
    private final NepsePriceInfoService nepsePriceInfoService ;
    private ObjectMapper objectMapper = new ObjectMapper();
    private TaskExecutor executor;

    public NepseCompanyServiceImpl(
        NepseCompanyRepository nepseCompanyRepository, TaskExecutor taskExecutor,
        CustomerShareLoanThreadService customerShareLoanThreadService,
        NepsePriceInfoService nepsePriceInfoService) {
        this.nepseCompanyRepository = nepseCompanyRepository;
        this.executor = taskExecutor;
        this.customerShareLoanThreadService = customerShareLoanThreadService;
        this.nepsePriceInfoService = nepsePriceInfoService;
    }

    @Override
    public List<NepseCompany> findAll() {
        return nepseCompanyRepository.findAll();
    }

    @Override
    public NepseCompany findOne(Long id) {
        return nepseCompanyRepository.getOne(id);
    }

    @Override
    public NepseCompany save(NepseCompany nepseCompany) {
        nepseCompany.setLastModifiedAt(new Date());
        if (nepseCompany.getId() == null) {
            nepseCompany.setStatus(Status.ACTIVE);
        }
        return nepseCompanyRepository.save(nepseCompany);
    }

    @Override
    public Page<NepseCompany> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return nepseCompanyRepository
                .nepseCompanyFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<NepseCompany> saveAll(List<NepseCompany> list) {
        return nepseCompanyRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> nepseStatusCount() {
        return nepseCompanyRepository.nepseCompanyStatusCount();
    }

    @Override
    public void saveList(List<NepseCompany> newNepseList) {
        List<NepseCompany> existingNepseList = nepseCompanyRepository.findAll();

        // Get unique list from excel
        newNepseList = newNepseList.stream()
                .filter(FilterJsonUtils.distinctByKey(n -> n.getCompanyCode()))
                .collect(Collectors.toList());

        // Get Company code list
        List<String> newCompanyCodeList = newNepseList.stream()
                .map(NepseCompany::getCompanyCode).collect(Collectors.toList());

        // get list company that is  in new nepse list
        existingNepseList = existingNepseList.stream()
            .filter(nepseCompany -> newCompanyCodeList.contains(nepseCompany.getCompanyCode()))
            .collect(Collectors.toList());

        nepseCompanyRepository.deleteAll(existingNepseList);
        nepseCompanyRepository.saveAll(newNepseList);
        executor.execute(customerShareLoanThreadService);
    }

    @Override
    public List<NepseCompany> getAllNepseBySearchDto(Object searchDto) {
        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        final NepseSpecBuilder nepseSpecBuilder = new NepseSpecBuilder(s);
        final Specification<NepseCompany> specification = nepseSpecBuilder.build();
        return nepseCompanyRepository.findAll(specification);

    }

    @Override
    public void  uploadNepseData(MultipartFile file, NepsePriceInfo nepsePriceInfo) {
           nepsePriceInfoService.deletePrevRecord();
           nepsePriceInfoService.save(nepsePriceInfo);
           List<NepseCompany> nepseCompanyList = NepseExcelReader.parseNepseCompanyFile(file);
           saveList(nepseCompanyList);
    }


}
