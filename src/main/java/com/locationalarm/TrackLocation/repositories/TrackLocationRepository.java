package com.locationalarm.TrackLocation.repositories;

import com.locationalarm.TrackLocation.entities.TrackLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackLocationRepository extends JpaRepository<TrackLocation,Long> {
    List<TrackLocation> findByUserId(String userId);
}
