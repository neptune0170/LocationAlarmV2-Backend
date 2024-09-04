package com.locationalarm.TrackLocation.controllers;

import com.locationalarm.TrackLocation.entities.TrackLocation;
import com.locationalarm.TrackLocation.services.TrackLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/track-location")
public class TrackLocationController {

    @Autowired
    private TrackLocationService trackLocationService;

    @PostMapping("/save")
    public TrackLocation saveTrackLocation(@RequestBody TrackLocation trackLocation) {
        return trackLocationService.saveTrackLocation(trackLocation);
    }

    @GetMapping("/user/{userId}")
    public List<TrackLocation> getTrackLocationsByUserId(@PathVariable String userId) {
        return trackLocationService.getTrackLocationsByUserId(userId);
    }
}