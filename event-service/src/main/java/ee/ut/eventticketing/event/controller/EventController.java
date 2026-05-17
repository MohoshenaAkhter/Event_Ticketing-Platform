package ee.ut.eventticketing.event.controller;

import ee.ut.eventticketing.event.model.Event;
import ee.ut.eventticketing.event.service.EventService;
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
    public Event create(@RequestBody Event event) {
        return service.create(event);
    }

    @GetMapping
    public List<Event> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Event getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        return service.update(id, event);
    }

    @PatchMapping("/{id}/status")
    public Event updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @PutMapping("/{id}/venue")
    public Event assignVenue(@PathVariable Long id, @RequestParam Long venueId) {
        return service.assignVenue(id, venueId);
    }
}