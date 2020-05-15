package com.grinnotech.patientsorig.util;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 *
 * @author Jacek Sztajnke
 */
@Builder
public class ValidationMessages {

    private String field;

    @Singular("message")
    private List<String> messages;

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
