package com.locationalarm.Events.services;

import com.locationalarm.Events.dtos.EventDetailResponse;


import java.util.Date;
import java.util.List;

public interface EventHeaderService {
    public long addEvent(String eventName, String adminMail, Date endTime, String locationName, String latitude, String longitude,boolean isAOTEnable);
    public String removeAttendee(List<String> email,long eventId);
    public String addAttendee(List<String> email, long eventId);
    public String deleteEvent(long eventId);
    public EventDetailResponse getEvent(long eventId);
    public String updateEvent(long eventId, String lat, String lng, String locationName, String eventName, Date eventEndTime, boolean isAOTEnable);
}
