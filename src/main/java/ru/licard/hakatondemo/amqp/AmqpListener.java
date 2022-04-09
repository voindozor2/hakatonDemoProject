package ru.licard.hakatondemo.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.licard.hakatondemo.dto.SendingDto;
import ru.licard.hakatondemo.service.DomainService;

@Component
@RequiredArgsConstructor
public class AmqpListener {

    private final DomainService domainService;

    @RabbitListener(queues = "hakatonDemoQueue")            // этот обработчик принимает сообщения и передает новое сообщение сервису для добавления
    private void hakatonDemoQueue(String message) {
        domainService.saveHakatonEntity(new SendingDto(message));
    }
}
