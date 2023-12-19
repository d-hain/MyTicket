package me.dave.myticket.repository;

import me.dave.myticket.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventsByFromAfter(Date after);
}
