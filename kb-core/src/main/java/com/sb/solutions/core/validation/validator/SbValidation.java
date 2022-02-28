package com.sb.solutions.core.validation.validator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.exception.handler.Violation;
import com.sb.solutions.core.validation.constraint.SbValid;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : Rujan Maharjan on  12/7/2020
 **/

@Aspect
@Component
@Slf4j
public class SbValidation {

    public static final String EMPTY_ = "%s Cannot be NUll";

    private ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));


    @Pointcut("execution(* com.sb.solutions..*.*(..))")
    public void serviceLayer() {
    }

    @Before(value = "serviceLayer() && @annotation(sbValid) && args(batch)")
    public void emptyValidate(JoinPoint pjp, Object batch,
                                                SbValid sbValid) {
        Map<String, Object> map;
        List<Violation> violations = new ArrayList<>();
        log.info("validate by::{} for object batch:::{}", sbValid.value(), batch);
        if (!StringUtils.isEmpty(sbValid.value())) {
            List<String> validatedBy = new ArrayList<>(Arrays.asList(sbValid.value().split(",")));
            map = mapper.convertValue(batch, Map.class);
            log.info("map data::{}", map.toString());
            validatedBy.forEach(v -> {
                Object value = map.get(v);
                if (ObjectUtils.isEmpty(value)) {
                    violations.add(new Violation(v, value, String.format(EMPTY_, v)));
                }
            });
            if (violations.size() > 0) {
                throw new ServiceValidationException("Invalid Request", violations);
            }
        }
    }

}
