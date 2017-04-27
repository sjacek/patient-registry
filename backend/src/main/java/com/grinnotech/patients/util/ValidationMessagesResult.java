package com.grinnotech.patients.util;

import ch.ralscha.extdirectspring.bean.ExtDirectStoreResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class ValidationMessagesResult<T> extends ExtDirectStoreResult<T> {

    private List<ValidationMessages> validations;

    public ValidationMessagesResult(T record) {
        super(record);
    }

    public ValidationMessagesResult(T record, List<ValidationMessages> validations) {
        super(record);
        setValidations(validations);
    }

    public List<ValidationMessages> getValidations() {
        return this.validations;
    }

    public final void setValidations(List<ValidationMessages> validations) {
        this.validations = validations;
        if (this.validations != null && !this.validations.isEmpty()) {
            setSuccess(Boolean.FALSE);
        }
    }

    public final void addValidation(ValidationMessages validation) {
        validations.add(validation);
        if (this.validations != null && !this.validations.isEmpty()) {
            setSuccess(Boolean.FALSE);
        }
    }

}
