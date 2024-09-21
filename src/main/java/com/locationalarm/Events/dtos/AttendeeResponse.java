package com.locationalarm.Events.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendeeResponse {
    private String email;
    private String name;
    private String AOT;
}
