package ru.licard.hakatondemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Value("${rabbit.host}")
    private String rabbitHost;

    @Value("${rabbit.port}")
    private int rabbitPort;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(rabbitHost,rabbitPort);
    }

    @Bean
    public Queue hackathonDemoQueue() {
        return QueueBuilder.durable("hackathonDemoQueue")
                .build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory =
                new SimpleRabbitListenerContainerFactory();
        rabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        rabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        rabbitListenerContainerFactory.setConcurrentConsumers(25);
        rabbitListenerContainerFactory.setMaxConcurrentConsumers(50);
//        rabbitListenerContainerFactory.setBatchSize(1);
        rabbitListenerContainerFactory.setPrefetchCount(2);
        rabbitListenerContainerFactory.setConsumerBatchEnabled(false);
        rabbitListenerContainerFactory.setDefaultRequeueRejected(false);
        return rabbitListenerContainerFactory;
    }
}