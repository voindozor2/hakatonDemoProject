package ru.licard.hakatondemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.licard.hakatondemo.data.HakatonEntityRepository;
import ru.licard.hakatondemo.domain.HakatonEntity;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final HakatonEntityRepository hakatonEntityRepository;

    @Override
    public void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
        }
    }
}
