package com.sb.solutions.core.validation.validator;

import java.util.Arrays;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.validation.constraint.FileFormatValid;


/**
 * @author Sunil Babu Shrestha
 * 11/2/2021
 */

@Component
public class FileValidator implements ConstraintValidator<FileFormatValid, MultipartFile> {

    private final List<String> ignoreFileContentType = Arrays.asList("image/svg+xml");

    @Override
    public void initialize(FileFormatValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile file,
        ConstraintValidatorContext constraintValidatorContext) {
        if(file.isEmpty()){
            throw new ServiceValidationException("Invalid Request File is Empty");
        }
        if(ignoreFileContentType.contains(file.getContentType())){
            throw new ServiceValidationException(String.format("%s file Format Not Supported",file.getContentType()));
        }
        return true;
    }


}
