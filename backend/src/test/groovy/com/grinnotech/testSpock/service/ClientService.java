package com.grinnotech.testSpock.service;

import org.jetbrains.annotations.Contract;

public class ClientService {

    private MessageService messageService;

    @Contract(pure = true)
    public ClientService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String greet(String name) {
        return messageService.getMessage() + "! What's up, " + name + "?";
    }

    public String introduceMe(String name, int age) {
        return messageService.getIntro(name, age) + " I'm a rockstar.";
    }
}
