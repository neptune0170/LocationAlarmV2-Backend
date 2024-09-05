package com.locationalarm.Events.services;

import com.locationalarm.Events.entities.EventHeader;

import java.util.Date;
import java.util.List;

public interface EventHeaderService {
    public long addEvent(String eventName, String adminMail, Date endTime, String latitude, String longitude,boolean isAOTEnable);
    public String removeAttendee(String email,long eventId);
    public String addAttendee(List<String> email, long eventId);
    public String deleteEvent(long eventId);
    public EventHeader getEvent(long eventId);
}
