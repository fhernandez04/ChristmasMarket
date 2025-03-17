package main.java.de.tum.cit.aet.pse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.java.de.tum.cit.aet.pse.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    
}
