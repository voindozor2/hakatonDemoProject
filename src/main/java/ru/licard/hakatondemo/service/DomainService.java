package ru.licard.hakatondemo.service;

import ru.licard.hakatondemo.dto.SendingDto;

public interface DomainService {
    void saveHakatonEntity(SendingDto sendingDto);
}
