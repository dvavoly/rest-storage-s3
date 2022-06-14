package org.example.storage.rest;

import org.example.storage.dto.EventDto;
import org.example.storage.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {

    private final EventService eventService;

    public EventRestControllerV1(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAllEvents();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEventById(id);
    }
}
