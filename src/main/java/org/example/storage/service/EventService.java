package org.example.storage.service;

import org.example.storage.dto.EventDto;
import org.example.storage.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final FileService fileService;
    private final UserService userService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventService(EventRepository eventRepository, FileService fileService, UserService userService) {
        this.eventRepository = eventRepository;
        this.fileService = fileService;
        this.userService = userService;
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> new EventDto(
                        event.getId(),
                        fileService.getFileById(event.getFileId()).fileName(),
                        userService.getUserById(event.getUserId()).getEmail(),
                        event.getUploadTime().format(formatter)))
                .toList();
    }

    public void deleteEventById(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
