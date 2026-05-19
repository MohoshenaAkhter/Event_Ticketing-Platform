package ee.ut.eventticketing.event.service;

import ee.ut.eventticketing.event.dto.VenueDTO;
import ee.ut.eventticketing.event.messaging.EventPublisher;
import ee.ut.eventticketing.event.model.Event;
import ee.ut.eventticketing.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repo;
    private final RestTemplate restTemplate;
    private final EventPublisher eventPublisher;

    @Value("${venue.service.url}")
    private String venueServiceUrl;

    public EventService(
            EventRepository repo,
            RestTemplate restTemplate,
            EventPublisher eventPublisher
    ) {
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
    }

    public Event create(Event event) {
        event.setStatus("DRAFT");

        Event savedEvent = repo.save(event);

        eventPublisher.publishEventCreated(savedEvent);

        return savedEvent;
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
        event.setCategory(updated.getCategory());
        event.setOrganizerId(updated.getOrganizerId());
        event.setVenueId(updated.getVenueId());
        event.setStartDateTime(updated.getStartDateTime());
        event.setEndDateTime(updated.getEndDateTime());
        event.setStatus(updated.getStatus());
        event.setPosterUrl(updated.getPosterUrl());

        return repo.save(event);
    }

    public Event updateStatus(Long id, String status) {
        Event event = repo.findById(id).orElseThrow();
        event.setStatus(status);
        return repo.save(event);
    }

    public Event assignVenue(Long eventId, Long venueId, String authorizationHeader) {
        Event event = repo.findById(eventId).orElseThrow();

        String url = venueServiceUrl + "/venues/" + venueId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<VenueDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                VenueDTO.class
        );

        VenueDTO venue = response.getBody();

        if (venue == null) {
            throw new RuntimeException("Venue not found");
        }

        event.setVenueId(venue.getId());

        return repo.save(event);
    }
}