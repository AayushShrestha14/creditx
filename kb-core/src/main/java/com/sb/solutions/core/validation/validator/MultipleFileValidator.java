package com.sb.solutions.core.validation.validator;

import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.validation.constraint.MultipleFileFormatValid;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ayonij Karki on 1/12/2022
 */

@Component
public class MultipleFileValidator implements ConstraintValidator<MultipleFileFormatValid, List<MultipartFile>> {
    private final List<String> ignoreFileContentType = Arrays.asList("image/svg+xml");

    @Override
    public void initialize(MultipleFileFormatValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<MultipartFile> value,
                           ConstraintValidatorContext context) {
        if (value.isEmpty()) {
            throw new ServiceValidationException("Invalid Request File is empty");
        }

        value.forEach(multipartFile -> {
            if (ignoreFileContentType.contains(multipartFile.getContentType())) {
                throw new ServiceValidationException(String.format("%s file Format Not Supported",multipartFile.getContentType()));
            }
        });

        return true;
    }
}
