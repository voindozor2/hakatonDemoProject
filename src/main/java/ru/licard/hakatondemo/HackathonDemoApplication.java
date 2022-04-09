package ru.licard.hakatondemo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.licard.hakatondemo.service.SenderService;

@SpringBootApplication
@RequiredArgsConstructor
public class HackathonDemoApplication implements CommandLineRunner {
    private final SenderService senderService;

    public static void main(String[] args) {
        SpringApplication.run(HackathonDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        senderService.sendTestMessages();
    }
}
