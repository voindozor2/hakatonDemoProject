package ru.licard.hakatondemo.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface SenderService {
    void sendTestMessages() throws JsonProcessingException;
}
