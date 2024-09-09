package com.locationalarm.Events.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EventDetailResponse {
    private String locationName;
    private String lat;
    private String lng;
    private String eventName;
    private String time;
    private boolean isAOTEnable;
    private String admin;
    private List<AttendeeResponse> attendees;
}
