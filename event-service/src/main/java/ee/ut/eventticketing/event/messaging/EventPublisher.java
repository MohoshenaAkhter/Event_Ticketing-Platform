package ee.ut.eventticketing.event.messaging;

import ee.ut.eventticketing.event.config.RabbitMQConfig;
import ee.ut.eventticketing.event.model.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEventCreated(Event event) {
        String message = "Event created: id="
                + event.getEventId()
                + ", title="
                + event.getTitle();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EVENT_EXCHANGE,
                RabbitMQConfig.EVENT_CREATED_ROUTING_KEY,
                message
        );
    }
}