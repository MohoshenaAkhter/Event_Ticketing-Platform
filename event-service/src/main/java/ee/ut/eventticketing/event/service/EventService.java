package ee.ut.eventticketing.event.service;

import ee.ut.eventticketing.event.dto.VenueDTO;
import ee.ut.eventticketing.event.model.Event;
import ee.ut.eventticketing.event.repository.EventRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repo;
    private final RestTemplate restTemplate;

    public EventService(EventRepository repo,
                        RestTemplate restTemplate) {

        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public Event create(Event event) {

        event.setStatus("DRAFT");

        return repo.save(event);
    }

    public List<Event> getAll() {
        return repo.findAll();
    }

    public Event getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Event update(Long id, Event updated) {

        Event event = repo.findById(id).orElseThrow();

        event.setTitle(updated.getTitle());
        event.setDescription(updated.getDescription());

        return repo.save(event);
    }

    public Event updateStatus(Long id, String status) {

        Event event = repo.findById(id).orElseThrow();

        event.setStatus(status);

        return repo.save(event);
    }

    // REAL MICROSERVICE INTEGRATION

    public Event assignVenue(Long eventId, Long venueId) {

        // CALL VENUE SERVICE

       String url =
        "http://venue-service:8084/venues/" + venueId;

        VenueDTO venue =
                restTemplate.getForObject(
                        url,
                        VenueDTO.class
                );

        // VALIDATE VENUE EXISTS

        if (venue == null) {
            throw new RuntimeException("Venue not found");
        }

        // FIND EVENT

        Event event =
                repo.findById(eventId)
                        .orElseThrow();

        // ASSIGN VENUE

        event.setVenueId(venue.getId());

        return repo.save(event);
    }
}