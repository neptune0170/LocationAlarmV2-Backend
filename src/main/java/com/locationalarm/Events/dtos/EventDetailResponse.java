package com.locationalarm.Events.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventDetailResponse {
    private String eventId;
    private String locationName;
    private String lat;
    private String lng;
    private String eventName;
    private String time;
    private boolean isAOTEnable;
    private String admin;
    private String adminName;
    private List<AttendeeResponse> attendees;
}
