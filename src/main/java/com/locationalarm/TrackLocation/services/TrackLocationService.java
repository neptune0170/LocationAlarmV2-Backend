package com.locationalarm.TrackLocation.services;

import com.locationalarm.TrackLocation.entities.TrackLocation;
import com.locationalarm.TrackLocation.repositories.TrackLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackLocationService {
   @Autowired
    private TrackLocationRepository trackLocationRepository;

   public TrackLocation saveTrackLocation(TrackLocation trackLocation){
       return trackLocationRepository.save(trackLocation);
   }

   public List<TrackLocation> getTrackLocationsByUserId(String userId){
       return trackLocationRepository.findByUserId(userId);
   }
}
