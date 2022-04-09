package ru.licard.hakatondemo.amqp;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

@Component
@RequiredArgsConstructor
public class AmqpListener {
    private final DomainService domainService;

    @RabbitListener(queues = "hackathonDemoQueue")
    private void hackathonDemoQueue(String message) {
        domainService.saveHackathonEntity(new SendingDto(message));
    }
}
