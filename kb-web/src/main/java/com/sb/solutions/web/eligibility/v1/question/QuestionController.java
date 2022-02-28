package com.sb.solutions.web.eligibility.v1.question;

import java.util.List;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.eligibility.question.entity.Question;
import com.sb.solutions.api.eligibility.question.service.QuestionService;
import com.sb.solutions.core.dto.RestResponseDto;

@RestController
@RequestMapping("/v1/loan-configs/{loanConfigId}/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public final ResponseEntity<?> addQuestions(
        @PathVariable long loanConfigId,
        @Valid @RequestBody List<Question> questions) {

        final List<Question> savedQuestions = questionService.save(questions);

        if (savedQuestions.size() == 0) {
            return new RestResponseDto().failureModel("Oops! Something went wrong.");
        }

        return new RestResponseDto().successModel(savedQuestions);
    }

    @GetMapping
    public final ResponseEntity<?> getQuestionsOfLoanConfig(@PathVariable long loanConfigId) {
        final List<Question> questions = questionService.findByLoanConfigId(loanConfigId);
        return new RestResponseDto().successModel(questions);
    }

    @PutMapping("/{id}")
    public final ResponseEntity<?> updateQuestions(
        @PathVariable long loanConfigId,
        @PathVariable long id,
        @Valid @RequestBody Question question) {

        final Question updatedQuestion = questionService.update(question);
        return new RestResponseDto().successModel(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public final ResponseEntity<?> deleteQuestion(@PathVariable long loanConfigId,
        @PathVariable long id) {
        questionService.delete(id);
        return new RestResponseDto().successModel("Successfully deleted.");
    }
}
