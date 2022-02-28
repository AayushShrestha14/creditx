package com.sb.solutions.api.crg.controller;

import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.api.crg.mapper.CrgQuestionMapper;
import com.sb.solutions.api.crg.service.CrgQuestionService;
import com.sb.solutions.core.dto.RestResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */

@RestController
@RequestMapping("/v1/{loanConfigId}/crg-questions")
public class CrgQuestionController {

    private final CrgQuestionService questionService;
    private final CrgQuestionMapper crgQuestionMapper;

    public CrgQuestionController(
            @Autowired CrgQuestionService questionService,
            @Autowired CrgQuestionMapper crgQuestionMapper) {
        this.questionService = questionService;
        this.crgQuestionMapper = crgQuestionMapper;
    }

    @PostMapping
    public final ResponseEntity<?> addQuestions(
            @Valid @RequestBody List<CrgQuestion> questions) {

        final List<CrgQuestion> savedQuestions = questionService.save(questions);

        if (savedQuestions.size() == 0) {
            return new RestResponseDto().failureModel("Oops! Something went wrong.");
        }

        return new RestResponseDto().successModel(savedQuestions);
    }

    @GetMapping
    public final ResponseEntity<?> getQuestionsFromLoanConfigId(
            @PathVariable Long loanConfigId) {
        final List<CrgQuestion> questions = questionService.findByLoanConfigId(loanConfigId);
        return new RestResponseDto().successModel(crgQuestionMapper.mapEntitiesToDtos(questions));
    }

    @PutMapping("/{id}")
    public final ResponseEntity<?> updateQuestions(
            @Valid @RequestBody CrgQuestion question) {

        final CrgQuestion updatedQuestion = questionService.update(question);
        return new RestResponseDto().successModel(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<?> deleteQuestion(
            @PathVariable long id) {
        questionService.delete(id);
        return new RestResponseDto().successModel("Successfully deleted.");
    }
}
