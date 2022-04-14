package ru.licard.hakatondemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.licard.hakatondemo.data.HackathonEntityRepository;
import ru.licard.hakatondemo.domain.HackathonEntity;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

@Service
@RequiredArgsConstructor
public class DomainServiceImpl implements DomainService {

    private final HackathonEntityRepository hackathonEntityRepository;

    // Запросы принимаются программой и записываются в очередь, но
    // после обрабатываются синхронно, что нам и требовалось для
    // корректной работы базы данных (да, потоков было все же несколько)

    @Override
    public synchronized void saveHackathonEntity(SendingDto sendingDto) {
        if (!hackathonEntityRepository.existsHackathonEntityByName(sendingDto.getName())) {
            hackathonEntityRepository.save(new HackathonEntity(sendingDto.getName()));
        }
    }
}
