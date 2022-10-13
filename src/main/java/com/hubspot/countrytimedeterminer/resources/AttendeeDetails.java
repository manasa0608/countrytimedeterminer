package com.hubspot.countrytimedeterminer.resources;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class AttendeeDetails {

    private Integer attendeeCount;
    private String name;
    private String startDate;
    private List<String> attendees;

}
