package main.java.de.tum.cit.aet.pse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.java.de.tum.cit.aet.pse.entity.Event;
import main.java.de.tum.cit.aet.pse.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    public Event save(Event newEvent) {
        return eventRepository.save(newEvent);
    }

    public Iterable<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event findById(int eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    public void deleteById(int eventId) {
        try {
            eventRepository.deleteById(eventId);
        }
        catch (Exception e) {
            System.err.println("Unable to delete Event with ID: " + eventId);
        }
    }
}
