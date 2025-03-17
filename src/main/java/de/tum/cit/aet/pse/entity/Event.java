package main.java.de.tum.cit.aet.pse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private EventType eventType;

    public Event() {
    }

    public Event(int id, String name, String description, EventType eventType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventType = eventType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public EventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", name=" + name + ", description=" + description + ", eventType=" + eventType + "]";
    }
}