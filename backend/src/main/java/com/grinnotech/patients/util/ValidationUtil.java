package com.grinnotech.patients.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Deprecated
public class ValidationUtil {

    public static <T> List<ValidationMessages> validateEntity(Validator validator, T entity, Class<?>... groups) {

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity, groups);
        Map<String, List<String>> fieldMessages = new HashMap<>();
        if (!constraintViolations.isEmpty()) {
            constraintViolations.forEach((constraintViolation) -> {
                String property = constraintViolation.getPropertyPath().toString();
                List<String> messages = fieldMessages.computeIfAbsent(property, k -> new ArrayList<>());
                messages.add(constraintViolation.getMessage());
            });
        }
        List<ValidationMessages> validationErrors = new ArrayList<>();
        fieldMessages.forEach((k, v) -> {
            ValidationMessages errors = new ValidationMessages();
            errors.setField(k);
            errors.setMessages(v.toArray(new String[v.size()]));
            validationErrors.add(errors);
        });

        return validationErrors;
    }

}
