package com.sb.solutions.api.nepseCompany.service;

import java.util.List;

import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.loan.service.CustomerShareLoanThreadService;
import com.sb.solutions.api.nepseCompany.entity.NepseMaster;
import com.sb.solutions.api.nepseCompany.repository.NepseMasterRepository;
import com.sb.solutions.core.enums.Status;

/**
 * @author Sunil Babu Shrestha on 1/17/2020
 */
@Service
public class NepseMasterServiceImpl implements NepseMasterService {
    private final NepseMasterRepository nepseMasterRepository;
    private final CustomerShareLoanThreadService customerShareLoanThreadService;
    private TaskExecutor executor;

    public NepseMasterServiceImpl(NepseMasterRepository nepseMasterRepository,
        TaskExecutor taskExecutor, CustomerShareLoanThreadService customerShareLoanThreadService) {
        this.nepseMasterRepository = nepseMasterRepository;
        this.executor = taskExecutor;
        this.customerShareLoanThreadService = customerShareLoanThreadService;
    }

    @Override
    public List<NepseMaster> findAll() {
        return nepseMasterRepository.findAll();
    }

    @Override
    public NepseMaster findOne(Long id) {
        return nepseMasterRepository.getOne(id);
    }

    @Override
    public NepseMaster save(NepseMaster nepseMaster) {
        // find existing active record and make it inactive
        NepseMaster existingActive = nepseMasterRepository.findByStatus(Status.ACTIVE);
        if (null != existingActive) {
            existingActive.setStatus(Status.INACTIVE);
            nepseMasterRepository.save(existingActive);
        }
        nepseMasterRepository.save(nepseMaster);
//        executor.execute(customerShareLoanThreadService);
        return nepseMaster;
    }

    @Override
    public Page<NepseMaster> findAllPageable(Object t, Pageable pageable) {
        return nepseMasterRepository.findAll(pageable);
    }

    @Override
    public List<NepseMaster> saveAll(List<NepseMaster> list) {
        return nepseMasterRepository.saveAll(list);
    }

    @Override
    public NepseMaster findActiveMasterRecord() {
        return nepseMasterRepository.findByStatus(Status.ACTIVE);
    }

    @Override
    public Page<NepseMaster> findNepseListOrderById(Object searchDto, Pageable pageable) {
        return nepseMasterRepository.findAllByOrderByIdDesc(pageable);
    }
}


