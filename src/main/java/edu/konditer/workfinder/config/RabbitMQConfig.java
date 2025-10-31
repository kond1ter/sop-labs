package edu.konditer.workfinder.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "vacancies-exchange";
    public static final String ROUTING_KEY_VACANCY_CREATED = "vacancy.created";
    public static final String ROUTING_KEY_VACANCY_DELETED = "vacancy.deleted";

    @Bean
    public TopicExchange booksExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }
}
