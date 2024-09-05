package com.locationalarm.Events.repositories;

import com.locationalarm.Events.entities.EventHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventHeaderRepo extends JpaRepository<EventHeader,Long> {
}
