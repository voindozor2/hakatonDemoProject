package ru.licard.hakatondemo.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import ru.licard.hakatondemo.service.SenderService;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendTestMessages() throws JsonProcessingException {
        byte[] array = new byte[14];
        new Random().nextBytes(array);
        String name = DigestUtils.md5DigestAsHex(array);

        for (int i = 0; i < 80; i++) {
            rabbitTemplate.convertAndSend("hakatonDemoQueue",name);     // вот этот сендер отправляет одинаковые сообщения
        }
    }
}
