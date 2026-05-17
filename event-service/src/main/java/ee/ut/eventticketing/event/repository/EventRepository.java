package ee.ut.eventticketing.event.repository;

import ee.ut.eventticketing.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}