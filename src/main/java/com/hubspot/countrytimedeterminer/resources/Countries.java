package com.hubspot.countrytimedeterminer.resources;

import lombok.Data;

import java.util.List;

@Data
public class Countries {
    private List<AttendeeDetails> attendeeDetailsList;
}
