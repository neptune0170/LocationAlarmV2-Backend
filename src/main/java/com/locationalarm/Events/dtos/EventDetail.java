package com.locationalarm.Events.dtos;

import lombok.Data;

@Data
public class EventDetail {
    private String eventName;
    private String lat;
    private String lng;
    private String time;
    private boolean isAOTEnable;
}
