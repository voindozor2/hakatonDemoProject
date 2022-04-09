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
    public void saveHakatonEntity(SendingDto sendingDto) {                                  // этот обработчик должен проверять, есть ли такая запись в БД,
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){       // и класть, только если ее нет
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));          // но почему-то он этого не делает
        }
    }
}
