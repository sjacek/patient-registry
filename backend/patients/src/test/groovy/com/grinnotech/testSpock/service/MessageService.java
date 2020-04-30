package com.grinnotech.testSpock.service;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Strings;

public class MessageService {
    public String getMessage() {
        return "Hello";
    }

    public String getMessage(String name) {
        checkArgument(!Strings.isNullOrEmpty(name), "name cannot be blank");

        return "Hello, " + name + "!";
    }

    public String getIntro(String name, int age) {
        return "Hello, my name is " + name + ". I'm " + age + " years old.";
    }
}
