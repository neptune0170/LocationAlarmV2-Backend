package com.locationalarm.Events.services.impl;

import com.locationalarm.Authentication.entities.User;
import com.locationalarm.Authentication.services.UserService;
import com.locationalarm.Events.entities.Attendee;
import com.locationalarm.Events.entities.EventHeader;
import com.locationalarm.Events.repositories.EventHeaderRepo;
import com.locationalarm.Events.services.EventHeaderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class EventHeaderServiceImpl implements EventHeaderService {
    private final UserService userService;
    private final EventHeaderRepo eventHeaderRepo;

    public EventHeaderServiceImpl(UserService userService, EventHeaderRepo eventHeaderRepo) {
        this.userService = userService;
        this.eventHeaderRepo = eventHeaderRepo;
    }

    @Override
    @Transactional
    public long addEvent(String eventName, String adminMail, Date endTime, String locationName, String latitude, String longitude, boolean isAOTEnable) {
        User user = userService.findByEmail(adminMail).orElse(null);
        if(user==null || user.isInAnEvent()) return 0;
        else{
            user.setInAnEvent(true);
            userService.updateUser(user);
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
            return eventHeader.getId();
        }
    }

    @Override
    public String removeAttendee(String email, long eventId) {
        return "";
    }
    @Transactional
    @Override
    public String addAttendee(List<String> emails, long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeader == null) return "No Event";
        List<Attendee> attendeeList = eventHeader.getAttendeeList();
        emails.stream().forEach(email->{
            User user = userService.findByEmail(email).orElse(null);
            if(user != null && !user.isInAnEvent()){
                user.setInAnEvent(true);
                userService.updateUser(user);
                attendeeList.add(Attendee.builder().userEmail(email).build());
            }
        });
        eventHeaderRepo.save(eventHeader);
        return "Success";
    }

    @Override
    public String deleteEvent(long eventId) {
        EventHeader eventHeader = eventHeaderRepo.findById(eventId).orElse(null);
        if(eventHeaderRepo!=null)eventHeaderRepo.delete(eventHeader);
        return "Success";
    }

    @Override
    public EventHeader getEvent(long eventId) {
        System.out.println(eventHeaderRepo.findById(eventId).orElse(null));
        return null;
    }
}
