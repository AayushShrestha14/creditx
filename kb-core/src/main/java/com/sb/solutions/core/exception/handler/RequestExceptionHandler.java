package com.sb.solutions.core.exception.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.NoContentException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.exception.LoanExistInUserException;

@ControllerAdvice
public class RequestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerHandler(NullPointerException error) {
        RestResponseDto restResponseDto = new RestResponseDto();

        logger.error("Validation Error", error);
        return restResponseDto.failureModel("There is null value in some of the field. Please Verify");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> formErrorHandler(MethodArgumentNotValidException error) {

        logger.error("Validation Error", error);

        final List<Violation> violations = processError(error);

        final Map<String, Object> response = Maps.newHashMap();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        if (null != violations) {
            response.put("message",
                    "Validation failed for " + error.getBindingResult().getObjectName());

            response.put("errorCount", error.getBindingResult().getErrorCount());
            response.put("errors", violations);
        } else {
            response.put("message", error.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> messageNotReadableExceptionHandler(
            HttpMessageNotReadableException ex) {

        logger.error("Can not parse request", ex);

        final List<Violation> violations = processMessageError(ex);

        final Map<String, Object> response = Maps.newHashMap();

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        if (null != violations) {
            response.put("message", "Can not convert to valid object");

            response.put("errorCount", violations.size());
            response.put("errors", violations);
        } else {
            response.put("message", ex.getLocalizedMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private List<Violation> processError(MethodArgumentNotValidException ex) {
        final BindingResult rs = ex.getBindingResult();

        if (rs instanceof BeanPropertyBindingResult && rs.hasErrors()) {
            final BeanPropertyBindingResult propertyResult = (BeanPropertyBindingResult) rs;

            final List<Violation> violations = Lists
                    .newArrayListWithCapacity(ex.getBindingResult().getErrorCount());

            for (FieldError error : propertyResult.getFieldErrors()) {
                final Violation violation = new Violation(error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage());

                violations.add(violation);
            }

            return violations;
        }

        return null;
    }

    private List<Violation> processMessageError(HttpMessageNotReadableException ex) {

        if (ex.getMostSpecificCause() instanceof InvalidFormatException) {

            final InvalidFormatException formatException = (InvalidFormatException) ex
                    .getMostSpecificCause();

            final String field = formatException.getPath().stream().map(Reference::getFieldName)
                    .collect(
                            Collectors.joining(","));

            final Violation violation = new Violation(field, formatException.getValue(),
                    formatException.getOriginalMessage());

            return Lists.newArrayList(violation);
        }

        return null;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationHandler(
            ConstraintViolationException ex) {

        logger.warn("Can not parse request", ex);
        String message = "Duplicate entry";

        final Map<String, Object> response = Maps.newHashMap();

        try {
            List<String> messageSplit = Stream.of(ex.getSQLException().getMessage().split("\\."))
                    .collect(Collectors.toList());
            message = messageSplit
                    .stream()
                    .filter(s -> s.contains("The duplicate key value is"))
                    .collect(Collectors.joining());
        } catch (Exception e) {
            logger.warn("unable to split sql error message {}", ex.getSQLException().getMessage());
        }
        response
                .put("message", message + " ,Please check and try again.");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<?> NoContentExceptionHandler(NoContentException error) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setMessage(error.getMessage());
        return new ResponseEntity<>(restResponseDto, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(LoanExistInUserException.class)
    public ResponseEntity<?> loanExistInUserExceptionHandler(LoanExistInUserException error) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setMessage(error.getMessage());
        restResponseDto.setDetail(error.getObject());
        return new ResponseEntity<>(restResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentExceptionHandler(IllegalArgumentException error) {
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setMessage(error.getMessage());
        return new ResponseEntity<>(restResponseDto, HttpStatus.BAD_REQUEST);
    }

}
