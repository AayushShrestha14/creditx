package com.sb.solutions.api.loan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.loan.entity.CombinedLoan;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.repository.CombinedLoanRepository;
import com.sb.solutions.api.loan.repository.specification.CombinedLoanSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
@Service
public class CombinedLoanServiceImpl extends BaseServiceImpl<CombinedLoan, Long> implements CombinedLoanService {

    private final CombinedLoanRepository repository;
    private final CustomerLoanService customerLoanService;

    protected CombinedLoanServiceImpl(
        CombinedLoanRepository repository,
        CustomerLoanService customerLoanService
    ) {
        super(repository);

        this.repository = repository;
        this.customerLoanService = customerLoanService;
    }

    @Override
    protected BaseSpecBuilder<CombinedLoan> getSpec(Map<String, String> filterParams) {
        return new CombinedLoanSpecBuilder(filterParams);
    }

    @Override
    public CombinedLoan save(CombinedLoan combinedLoan) {
        CombinedLoan saved = repository.save(combinedLoan);
        List<CustomerLoan> loans = new ArrayList<>();
        combinedLoan.getLoans().forEach(l -> {
            CustomerLoan loan = customerLoanService.findOne(l.getId());
            loan.setCombinedLoan(saved);
            loans.add(loan);
        });
        // remove from existing combined loan
        List<CustomerLoan> existingCombinedLoans = customerLoanService
            .findByCombinedLoanId(saved.getId());
        existingCombinedLoans.forEach(l -> {
            if (loans.stream().mapToLong(CustomerLoan::getId).noneMatch(id -> id == l.getId())) {
                l.setCombinedLoan(null);
                loans.add(l);
            }
        });
        customerLoanService.saveAll(loans);
        if (combinedLoan.getLoans().isEmpty()) {
            repository.deleteById(saved.getId());
        }
        return addCombinedLoanList(saved);
    }

    @Override
    public Optional<CombinedLoan> findOne(Long aLong) {
        Optional<CombinedLoan> combinedLoan = repository.findById(aLong);
        return combinedLoan.map(this::addCombinedLoanList);
    }

    private CombinedLoan addCombinedLoanList(CombinedLoan combinedLoan) {
        List<CustomerLoan> loans = customerLoanService.findByCombinedLoanId(combinedLoan.getId())
            .stream()
            .peek(loan -> loan.setCombinedLoan(null))   // avoid circular data fetch
            .collect(Collectors.toList());
        combinedLoan.setLoans(loans);
        return combinedLoan;
    }
}
