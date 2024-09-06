package com.locationalarm.Events.repositories;

import com.locationalarm.Events.entities.Attendee;
import com.locationalarm.Events.entities.EventHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendeeRepo extends JpaRepository<Attendee,Long> {
    @Query("SELECT a FROM Attendee a WHERE a.userEmail IN (:names) and a.eventHeader = :id")
    List<Attendee> findByNamesandEventId(@Param("names") List<String> names,@Param("id") EventHeader eventHeader);
}
