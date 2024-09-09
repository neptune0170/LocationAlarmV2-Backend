package com.locationalarm.Events.dtos;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ResponseDto {
    private String status;
    private String response;
}
