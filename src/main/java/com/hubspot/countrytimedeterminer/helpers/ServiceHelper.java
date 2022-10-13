package com.hubspot.countrytimedeterminer.helpers;

import com.hubspot.countrytimedeterminer.resources.AttendeeDetails;
import com.hubspot.countrytimedeterminer.resources.Countries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceHelper {

    @Autowired DateHelper dateHelper;

    public Countries buildAttendeeDetails(
            Map<String, List<String>> attendeeDetails, Map<String, Date> earliestDateCountryMap, Set<String> nullCountries) {
        List<AttendeeDetails> attendeeDetailsList = new ArrayList<>();
        for (Map.Entry<String, Date> entry : earliestDateCountryMap.entrySet()) {

            AttendeeDetails attendeeDetails1 =
                    AttendeeDetails.builder()
                            .startDate(dateHelper.convertDateToStringFormat(entry.getValue()))
                            .name(entry.getKey())
                            .attendeeCount(attendeeDetails.get(entry.getKey()).size())
                            .attendees(attendeeDetails.get(entry.getKey()))
                            .build();
            attendeeDetailsList.add(attendeeDetails1);
        }

        for (String countriesWithNoConsecutiveDates : nullCountries) {

            AttendeeDetails attendeeDetails1 =
                    AttendeeDetails.builder()
                            .startDate(null)
                            .name(countriesWithNoConsecutiveDates)
                            .attendeeCount(0)
                            .attendees(new ArrayList<>())
                            .build();
            attendeeDetailsList.add(attendeeDetails1);
        }

        Countries countries = new Countries();
        countries.setAttendeeDetailsList(attendeeDetailsList);

        return countries;
    }
}
