package ee.ut.eventticketing.event.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EVENT_EXCHANGE = "event.exchange";
    public static final String EVENT_CREATED_ROUTING_KEY = "event.created";

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange(EVENT_EXCHANGE);
    }
}