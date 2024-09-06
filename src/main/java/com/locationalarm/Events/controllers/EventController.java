package com.locationalarm.Events.controllers;

import com.locationalarm.Authentication.services.JwtService;
import com.locationalarm.Events.dtos.AtendeeList;
import com.locationalarm.Events.dtos.EventDetail;
import com.locationalarm.Events.dtos.EventDetailResponse;
import com.locationalarm.Events.dtos.ResponseDto;
import com.locationalarm.Events.services.EventHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/event")
public class EventController {
    private final JwtService jwtService;
    private final EventHeaderService eventHeaderService;

    public EventController(JwtService jwtService, EventHeaderService eventHeaderService) {
        this.jwtService = jwtService;
        this.eventHeaderService = eventHeaderService;
    }

    @PostMapping
    public ResponseEntity<ResponseDto> addEvent(@RequestBody EventDetail eventDetail, HttpServletRequest request) {
        String email = jwtService.extractUsername(request.getHeader("Authorization").substring(7));
        //date format will be dd/MM/yyyy HH:mm:ss

        //System.out.println(email);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = sdf.parse(eventDetail.getTime());
            long eventId = eventHeaderService.addEvent(eventDetail.getEventName(), email, date, eventDetail.getLocationName() ,  eventDetail.getLat(), eventDetail.getLng(), eventDetail.isAOTEnable());
            return ResponseEntity.ok(ResponseDto.builder().code("200").message(String.valueOf(eventId)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder().code("500").message(String.valueOf("Wrong Date Format")).build());
        }
    }
    @PutMapping("/{eventId}/attendee")
    public ResponseEntity<ResponseDto> addAttendee(@PathVariable long eventId, @RequestBody AtendeeList atendeeList){
        return ResponseEntity.ok(ResponseDto.builder().code("200").message(eventHeaderService.addAttendee(atendeeList.getEmails(),eventId)).build());
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<ResponseDto> deleteEvent(@PathVariable long eventId){
        return ResponseEntity.ok(ResponseDto.builder().code("200").message(eventHeaderService.deleteEvent(eventId)).build());
    }
    @PutMapping("/{eventId}/event")
    public ResponseEntity<ResponseDto> updateEventDetail(@PathVariable long eventId, @RequestBody EventDetail eventDetail){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = sdf.parse(eventDetail.getTime());
            return ResponseEntity.ok(ResponseDto.builder().code("200").message(eventHeaderService.updateEvent(eventId,eventDetail.getLat(),eventDetail.getLng(),eventDetail.getLocationName(),eventDetail.getEventName(),date,eventDetail.isAOTEnable())).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder().code("500").message(String.valueOf("Wrong Date Format")).build());
        }
    }
    @GetMapping("/{eventId}")
    public EventDetailResponse getEventData(@PathVariable long eventId){
        return eventHeaderService.getEvent(eventId);
    }
    @DeleteMapping("/{eventId}/attendee")
    public ResponseEntity<ResponseDto> deleteAttendeeFromEvent(@PathVariable long eventId, @RequestBody AtendeeList atendeeList){
        return ResponseEntity.ok(ResponseDto.builder().code("200").message(eventHeaderService.removeAttendee(atendeeList.getEmails(),eventId)).build());
    }
}

