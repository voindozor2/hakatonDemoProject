package ru.licard.hakatondemo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.licard.hakatondemo.service.SenderService;

@SpringBootApplication
@RequiredArgsConstructor
public class HakatonDemoApplication implements CommandLineRunner {
    private final SenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(HakatonDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        senderService.sendTestMessages();
    }
}
