package com.sb.solutions.core.exception.handler;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.sb.solutions.core.exception.ServiceValidationException;

@ControllerAdvice
public class ServiceValidationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServiceValidationHandler.class);

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity<?> formErrorHandler(ServiceValidationException error) {

        logger.error("Validation Error", error);

        final List<Violation> violations = processError(error);

        final Map<String, Object> response = Maps.newHashMap();

        if (null != violations) {
            response.put("message",
                "Validation failed for " + error.getMessage());

            response.put("errorCount", violations.size());
            response.put("errors", violations);
        } else {
            response.put("message", error.getMessage());
        }

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private List<Violation> processError(ServiceValidationException error) {
        if (CollectionUtils.isNotEmpty(error.getViolations())) {
            return error.getViolations();
        }

        return null;
    }

}
