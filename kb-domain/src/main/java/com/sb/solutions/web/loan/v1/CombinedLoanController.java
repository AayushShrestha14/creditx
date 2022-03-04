package com.sb.solutions.web.loan.v1;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.loan.entity.CombinedLoan;
import com.sb.solutions.api.loan.service.CombinedLoanService;
import com.sb.solutions.core.dto.RestResponseDto;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
@RestController
@RequestMapping(CombinedLoanController.URL)
public class CombinedLoanController {

    static final String URL = "/v1/combined-loan";
    private final CombinedLoanService service;

    public CombinedLoanController(CombinedLoanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody CombinedLoan combinedLoan) {
        return new RestResponseDto().successModel(service.save(combinedLoan));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<CombinedLoan> combinedLoan = service.findOne(id);

        if (!combinedLoan.isPresent()) {
            return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "Loan not found");
        }

        return new RestResponseDto().successModel(combinedLoan.get());
    }

}
