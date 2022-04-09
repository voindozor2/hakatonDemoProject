package ru.licard.hakatondemo.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.licard.hakatondemo.data.HakatonEntityRepository;
import ru.licard.hakatondemo.domain.HakatonEntity;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

@Service
@RequiredArgsConstructor
@EnableTransactionManagement
public class DomainServiceImpl implements DomainService {

    private final HakatonEntityRepository hakatonEntityRepository;

    // этот обработчик проверяет что записи с таким ID в БД нет,
    // после чего записывает новую запись

    // т. к. Spring параллелит процессы самостоятельно,
    // а проверка и запись производятся двумя разными вызовами,
    // получается конкурентность, на момент проверки записи в БД нет
    // а на момент попытки записать уже появилась

    // я решил задачу по тупому, запретив параллельное выполнение
    // этого метода словом synchronized,
    // потому что у меня не работают транзакции
    
    @Override
    public synchronized void saveHakatonEntity(SendingDto sendingDto) {
        if(!hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName())) {
            System.out.println(sendingDto.getName() + hakatonEntityRepository.existsHakatonEntityByName(sendingDto.getName()));
            hakatonEntityRepository.save(new HakatonEntity(sendingDto.getName()));
        }
    }
}
