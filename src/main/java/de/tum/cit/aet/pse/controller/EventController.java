package main.java.de.tum.cit.aet.pse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.java.de.tum.cit.aet.pse.entity.Event;
import main.java.de.tum.cit.aet.pse.service.EventService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/event")
public class EventController {
    
    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<Iterable<Event>> getAllEvent() {
        Iterable<Event> allEvents = eventService.findAll();
        return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Event event = eventService.findById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestParam Event event) {
        Event newEvent = eventService.save(event);
        return new ResponseEntity<>(newEvent, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestParam Event event) {
        event.setId(id);
        Event updatedEvent = eventService.save(event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }  
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable int id) {
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
