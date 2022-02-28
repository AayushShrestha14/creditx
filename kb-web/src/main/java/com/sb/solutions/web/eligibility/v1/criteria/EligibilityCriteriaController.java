package com.sb.solutions.web.eligibility.v1.criteria;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.api.eligibility.criteria.service.EligibilityCriteriaService;
import com.sb.solutions.api.eligibility.utility.EligibilityUtility;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.ArithmeticExpressionUtils;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@RequestMapping(EligibilityCriteriaController.URL)
public class EligibilityCriteriaController {

    public static final String URL = "/v1/eligibility-criterias";

    private final Logger logger = LoggerFactory.getLogger(EligibilityCriteriaController.class);

    private final EligibilityCriteriaService eligibilityCriteriaService;

    public EligibilityCriteriaController(EligibilityCriteriaService eligibilityCriteriaService) {
        this.eligibilityCriteriaService = eligibilityCriteriaService;
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @GetMapping
    public final ResponseEntity<?> getPageableEligibilityCriteria(@RequestParam("page") int page,
        @RequestParam("size") int size) {
        logger.debug("REST request to get all the eligibility criteria.");
        final Page<EligibilityCriteria> eligibilityCriteria = eligibilityCriteriaService
            .findAllPageable(null,
                PaginationUtils.pageable(page, size));
        return new RestResponseDto().successModel(eligibilityCriteria);
    }

    @GetMapping(path = "/{id}")
    public final ResponseEntity<?> getEligibilityCriteria(@PathVariable long id) {
        logger.debug("REST request to get criteria with id [{}].", id);
        final EligibilityCriteria eligibilityCriteria = eligibilityCriteriaService.findOne(id);
        if (eligibilityCriteria == null) {
            return new RestResponseDto().failureModel("Not Found.");
        }
        return new RestResponseDto().successModel(eligibilityCriteria);
    }

    @PostMapping
    public final ResponseEntity<?> saveEligibilityCriteria(
        @RequestBody EligibilityCriteria eligibilityCriteria) {
        logger.debug("REST request to save the eligibility criteria.");
        try {
            String mockFormula = EligibilityUtility
                .convertToMockFormula(eligibilityCriteria.getFormula());
            ArithmeticExpressionUtils.parseExpression(mockFormula);
            final EligibilityCriteria savedEligibilityCriteria = eligibilityCriteriaService
                .save(eligibilityCriteria);
            if (savedEligibilityCriteria == null) {
                return new RestResponseDto().failureModel("Oops something went wrong.");
            } else {
                return new RestResponseDto().successModel(savedEligibilityCriteria);
            }
        } catch (SpelParseException spelParseException) {
            return new RestResponseDto()
                .failureModel("Some wrong with expression you entered. Please verify.");
        }

    }

    @PutMapping(path = "/{id}")
    public final ResponseEntity<?> updateEligibilityCriteria(@PathVariable long id,
        @RequestBody EligibilityCriteria eligibilityCriteria) {
        logger.debug("REST request to update the eligibility criteria.");
        String message = "";
        try {
            String mockFormula = EligibilityUtility
                .convertToMockFormula(eligibilityCriteria.getFormula());
            ArithmeticExpressionUtils.parseExpression(mockFormula);
        } catch (SpelParseException spelParseException) {
            message = "Some wrong with expression you entered. Please verify.";
        }
        EligibilityCriteria updatedEligibilityCriteria = null;
        if (StringUtils.isEmpty(message)) {
            updatedEligibilityCriteria = eligibilityCriteriaService
                .update(eligibilityCriteria,
                    id);
            if (updatedEligibilityCriteria == null) {
                message = "Failed to update Eligibility Criteria.";
            }

        }
        if (StringUtils.isEmpty(message)) {
            return new RestResponseDto().successModel(updatedEligibilityCriteria);
        }

        return new RestResponseDto().failureModel(message);


    }

    @DeleteMapping(path = "/{id}")
    public final ResponseEntity<?> deleteEligibilityCriteria(@PathVariable long id) {
        logger.debug("REST request to delete eligibility criteria.");
        eligibilityCriteriaService.delete(id);
        return new RestResponseDto().successModel("Successfully deleted.");
    }

}
