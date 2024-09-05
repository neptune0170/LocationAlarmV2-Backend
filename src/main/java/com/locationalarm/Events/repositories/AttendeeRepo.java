package com.locationalarm.Events.repositories;

import com.locationalarm.Events.entities.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepo extends JpaRepository<Attendee,Long> {
}
