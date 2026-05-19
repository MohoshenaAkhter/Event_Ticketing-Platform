package ee.ut.eventticketing.event.controller;

import ee.ut.eventticketing.event.model.Event;
import ee.ut.eventticketing.event.service.EventService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Event create(@RequestBody Event event) {
        return service.create(event);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'CUSTOMER', 'ADMIN')")
    public List<Event> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'CUSTOMER', 'ADMIN')")
    public Event getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        return service.update(id, event);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Event updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @PutMapping("/{id}/venue")
    @PreAuthorize("hasRole('ADMIN')")
    public Event assignVenue(
            @PathVariable Long id,
            @RequestParam Long venueId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        return service.assignVenue(id, venueId, authorizationHeader);
    }
}