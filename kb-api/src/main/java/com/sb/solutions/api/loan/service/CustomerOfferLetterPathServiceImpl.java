package com.sb.solutions.api.loan.service;

import com.sb.solutions.api.loan.entity.CustomerOfferLetterPath;
import com.sb.solutions.api.loan.repository.CustomerOfferLetterPathRepository;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Rujan Maharjan on  11/25/2020
 **/
@Service
public class CustomerOfferLetterPathServiceImpl implements CustomerOfferLetterPathService {
    private final CustomerOfferLetterPathRepository customerOfferLetterPathRepository;
    private final UserService userService;

    public CustomerOfferLetterPathServiceImpl(
            CustomerOfferLetterPathRepository customerOfferLetterPathRepository,
            UserService userService) {
        this.customerOfferLetterPathRepository = customerOfferLetterPathRepository;
        this.userService = userService;
    }

    @Override
    public List<CustomerOfferLetterPath> findAll() {
        return customerOfferLetterPathRepository.findAll();
    }

    @Override
    public CustomerOfferLetterPath findOne(Long id) {
        return customerOfferLetterPathRepository.findById(id).get();
    }

    @Override
    public CustomerOfferLetterPath save(CustomerOfferLetterPath customerOfferLetterPath) {
        return customerOfferLetterPathRepository.save(customerOfferLetterPath);
    }

    @Override
    public Page<CustomerOfferLetterPath> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CustomerOfferLetterPath> saveAll(List<CustomerOfferLetterPath> list) {
        return null;
    }

    @Override
    public String updateApproveStatusAndApprovedBy(List<Long> ids) {
        final User user = userService.getAuthenticatedUser();
        customerOfferLetterPathRepository.updateApproveStatusAndApproveBy(true, user, ids);
        return "SUCCESSFULLY APPROVED";
    }
}
