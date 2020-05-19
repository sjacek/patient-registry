package com.grinnotech.patientsorig.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Deprecated
public class ValidationUtil {

    public static <T> List<ValidationMessages> validateEntity(Validator validator, T entity, Class<?>... groups) {

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity, groups);
        if (constraintViolations.isEmpty())
            return new ArrayList<>();

        Map<String, List<String>> fieldMessages = new HashMap<>();
        constraintViolations.forEach((constraintViolation) -> {
            String property = constraintViolation.getPropertyPath().toString();
            List<String> messages = fieldMessages.computeIfAbsent(property, k -> new ArrayList<>());
            messages.add(constraintViolation.getMessage());
        });

        return fieldMessages.entrySet().stream().map(e ->
                ValidationMessages.builder().field(e.getKey()).messages(e.getValue()).build())
                .collect(toList());
    }

}
