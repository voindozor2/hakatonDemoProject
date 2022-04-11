package ru.licard.hakatondemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.licard.hakatondemo.data.HakatonEntityRepository;
import ru.licard.hakatondemo.domain.HakatonEntity;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final HakatonEntityRepository hakatonEntityRepository;
    private final EntityManager entityManager;

    @Override
    public void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())){
//            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
            hakatonEntityRepository.insertNewEntity(sendingDto.getName());
        }
    }
}
