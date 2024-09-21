package com.locationalarm.Events.services.impl;

import com.locationalarm.Authentication.entities.User;
import com.locationalarm.Authentication.services.UserService;
import com.locationalarm.Events.dtos.AttendeeResponse;
import com.locationalarm.Events.dtos.EventDetailResponse;
import com.locationalarm.Events.entities.Attendee;
import com.locationalarm.Events.entities.EventHeader;
import com.locationalarm.Events.repositories.AttendeeRepo;
import com.locationalarm.Events.repositories.EventHeaderRepo;
import com.locationalarm.Events.services.EventHeaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class EventHeaderServiceImpl implements EventHeaderService {
    private final UserService userService;
    private final EventHeaderRepo eventHeaderRepo;
    private final AttendeeRepo attendeeRepo;

    public EventHeaderServiceImpl(UserService userService, EventHeaderRepo eventHeaderRepo, AttendeeRepo attendeeRepo) {
        this.userService = userService;
        this.eventHeaderRepo = eventHeaderRepo;
        this.attendeeRepo = attendeeRepo;
    }

    @Override
    @Transactional
    public long addEvent(String eventName, String adminMail, Date endTime, String locationName, String latitude, String longitude, boolean isAOTEnable) {
        User user = userService.findByEmail(adminMail).orElse(null);
        if(user==null || user.getEventId()!=0) return 0;
        else{
            List<Attendee> attendeeList = Arrays.asList(new Attendee[]{Attendee.builder().userEmail(adminMail).build()});
            EventHeader eventHeader = EventHeader
                    .builder()
                    .eventName(eventName)
                    .eventEndTime(endTime)
                    .adminEmail(adminMail)
                    .locationName(locationName)
                    .latitude(latitude)
                    .longitude(longitude)
                    .isAOTEnable(isAOTEnable)
                    .attendeeList(attendeeList).build();
            eventHeader = eventHeaderRepo.save(eventHeader);
            user.setEventId(eventHeader.getId());
            userService.updateUser(user);
            return eventHeader.getId();
        }
    }

    @Override
    public String removeAttendee(List<String> emails, long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeader == null) return "nothing";
       List<Attendee> attendeeList = attendeeRepo.findByNamesandEventId(emails,eventHeader);
       if(attendeeList==null) return "NO Attendees";
       attendeeList.stream().forEach(attendee -> {
           attendeeRepo.delete(attendee);
       });
        return "Success";
    }
    @Transactional
    @Override
    public String addAttendee(List<String> emails, long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if (eventHeader == null) {
            return "No Event";  // Event does not exist
        }

        List<Attendee> attendeeList = eventHeader.getAttendeeList();
        for (String email : emails) {
            User user = userService.findByEmail(email).orElse(null);
            if (user != null) {
                if (user.getEventId() != 0) {
                    // User is already part of another event, return "failed"
                    return "failed";
                } else {
                    // Add user to the event
                    user.setEventId(eventId);
                    userService.updateUser(user);
                    attendeeList.add(Attendee.builder().userEmail(email).build());
                }
            } else {
                // User not found
                return "user_not_found";
            }
        }
        eventHeaderRepo.save(eventHeader);
        return "success";
    }



    @Override
    public String deleteEvent(long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeaderRepo!=null)eventHeaderRepo.delete(eventHeader);
        return "Success";
    }

    @Override
    public EventDetailResponse getEvent(long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeader == null) return null;

        //fetching andmi's name from UserService

        User admin = userService.findByEmail(eventHeader.getAdminEmail()).orElse(null);
        String adminName = (admin!=null ) ? admin.getFullName() : "Unknown Admin";

        List<Attendee> attendeeList = eventHeader.getAttendeeList();
        List<AttendeeResponse> attendeeResponses = new LinkedList<>();
        attendeeList.stream().forEach(attendee -> {
            if(!attendee.getUserEmail().equals(eventHeader.getAdminEmail()))
            {
                User attendeeUser = userService.findByEmail(attendee.getUserEmail()).orElse(null);
                String attendeeName = (attendeeUser != null ) ? attendeeUser.getFullName() :"Unknown Attendee";

                attendeeResponses.add(AttendeeResponse.builder()
                        .email(attendee.getUserEmail())
                                .name(attendeeName)
                        .AOT("").build());
            }

        });
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
        return EventDetailResponse.builder()
                .eventId(String.valueOf(eventId))
                .admin(eventHeader.getAdminEmail())
                .adminName(adminName)
                .eventName(eventHeader.getEventName())
                .attendees(attendeeResponses)
                .time(sdf.format(eventHeader.getEventEndTime()))
                .lat(eventHeader.getLatitude())
                .lng(eventHeader.getLongitude())
                .locationName(eventHeader.getLocationName())
                .isAOTEnable(eventHeader.isAOTEnable())
                .build();
    }

    @Override
    public String updateEvent(long eventId, String lat, String lng, String locationName, String eventName, Date eventEndTime, boolean isAOTEnable) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeader == null) return "No Event Exit For this user";
        eventHeader.setEventName(eventName);
        eventHeader.setLongitude(locationName);
        eventHeader.setLongitude(lng);
        eventHeader.setLatitude(lat);
        eventHeader.setEventEndTime(eventEndTime);
        eventHeader.setAOTEnable(isAOTEnable);
        eventHeaderRepo.save(eventHeader);
        return "Success";
    }
}
