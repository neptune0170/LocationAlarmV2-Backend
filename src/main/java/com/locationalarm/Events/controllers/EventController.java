package com.locationalarm.Events.controllers;

import com.locationalarm.Authentication.services.JwtService;
import com.locationalarm.Events.dtos.AtendeeList;
import com.locationalarm.Events.dtos.EventDetail;
import com.locationalarm.Events.dtos.EventDetailResponse;
import com.locationalarm.Events.dtos.ResponseDto;
import com.locationalarm.Events.services.EventHeaderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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
            return ResponseEntity.ok(ResponseDto.builder().status("200").response(String.valueOf(eventId)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder().status("500").response(String.valueOf("Wrong Date Format")).build());
        }
    }

    @PutMapping("/addAttendee/{eventId}")
    public ResponseEntity<ResponseDto> addAttendee(@PathVariable long eventId, @RequestBody AtendeeList atendeeList){
        String result = eventHeaderService.addAttendee(atendeeList.getEmails(), eventId);

        if (result.equals("success")) {
            // Return HTTP 200 OK for successful addition
            return ResponseEntity.ok(
                    ResponseDto.builder().status("200").response(result).build()
            );
        } else if (result.equals("failed")) {
            // Return HTTP 409 Conflict if a user is already in another event
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    ResponseDto.builder().status("409").response("User already part of another event").build()
            );
        } else if (result.equals("user_not_found")) {
            // Return HTTP 404 Not Found if user does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseDto.builder().status("404").response("User not found").build()
            );
        } else {
            // Return HTTP 400 Bad Request for any other unexpected cases
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDto.builder().status("400").response("Unexpected error").build()
            );
        }
    }


    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<ResponseDto> deleteEvent(@PathVariable long eventId){
        return ResponseEntity.ok(ResponseDto.builder().status("200").response(eventHeaderService.deleteEvent(eventId)).build());
    }
    @PutMapping("/updateEventDetails/{eventId}")
    public ResponseEntity<ResponseDto> updateEventDetail(@PathVariable long eventId, @RequestBody EventDetail eventDetail){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date date = sdf.parse(eventDetail.getTime());
            return ResponseEntity.ok(ResponseDto.builder().status("200").response(eventHeaderService.updateEvent(eventId,eventDetail.getLat(),eventDetail.getLng(),eventDetail.getLocationName(),eventDetail.getEventName(),date,eventDetail.isAOTEnable())).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseDto.builder().status("500").response(String.valueOf("Wrong Date Format")).build());
        }
    }
    @GetMapping("/{eventId}")
    public EventDetailResponse getEventData(@PathVariable long eventId){
        return eventHeaderService.getEvent(eventId);
    }
    @DeleteMapping("/removeAttendee/{eventId}")
    public ResponseEntity<ResponseDto> deleteAttendeeFromEvent(@PathVariable long eventId, @RequestBody AtendeeList atendeeList){
        return ResponseEntity.ok(ResponseDto.builder().status("200").response(eventHeaderService.removeAttendee(atendeeList.getEmails(),eventId)).build());
    }
}

