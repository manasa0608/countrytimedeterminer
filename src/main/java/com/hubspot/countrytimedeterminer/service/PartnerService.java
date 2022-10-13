package com.hubspot.countrytimedeterminer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.countrytimedeterminer.constants.Endpoints;
import com.hubspot.countrytimedeterminer.helpers.DateHelper;
import com.hubspot.countrytimedeterminer.helpers.ServiceHelper;
import com.hubspot.countrytimedeterminer.resources.Countries;
import com.hubspot.countrytimedeterminer.resources.Partner;
import com.hubspot.countrytimedeterminer.resources.Partners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartnerService {

  @Autowired DateHelper dateHelper;

  @Autowired ServiceHelper serviceHelper;

  private static final Logger logger = LoggerFactory.getLogger(PartnerService.class);

  public Partners getPartnerDetails() {

    try {
      logger.info("Inside getPartnerDetails");
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(Endpoints.BASE_URL + Endpoints.GET_ENDPOINT))
              .GET()
              .build();

      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      return new ObjectMapper().readValue(response.body(), Partners.class);

    } catch (IOException | InterruptedException | URISyntaxException exception) {
      logger.error(
          "Exception ocurred while calling the partner details API. Details are -"
              + exception.getStackTrace());
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Unable to fetch partner details");
    }
  }

  public void determinePartnerDatesAvailability() {
    try {

      Map<String, List<String>> countryWiseMapping = new HashMap<>();
      Map<String, Date> earliestDateCountryMap;
      Map<String, List<String>> attendeeDetails = new HashMap<>();
      Set<String> nullCountries = new HashSet<>();

      logger.info("In determinePartnerDatesAvailability method");

      // Calls API to get client/Partner data and available dates
      Partners partners = getPartnerDetails();

      // check for consecutive and filter
      ArrayList<Partner> newPartners = new ArrayList<>();
      partners
          .getPartners()
          .forEach(
              partner -> {
                if (dateHelper.checkConsecutiveDatesCriteria(partner)) {
                  newPartners.add(partner);
                } else {
                  nullCountries.add(partner.getCountry());
                }
              });

      // grouped on country and available dates
      newPartners.forEach(
          partner ->
              countryWiseMapping.put(
                  partner.getCountry(),
                  Collections.unmodifiableList(
                      countryWiseMapping.getOrDefault(
                          partner.getCountry(), partner.getAvailableDates()))));

      // determine early date for each country
      earliestDateCountryMap =
          countryWiseMapping.entrySet().stream()
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey,
                      entry -> dateHelper.determineEarliestDate(entry.getValue()),
                      (a, b) -> b));

      // Add all partners who have the earliest date as their available date
      newPartners.stream()
          .filter(
              partner ->
                  earliestDateCountryMap.containsKey(partner.getCountry())
                      && partner
                          .getAvailableDates()
                          .contains(
                              dateHelper.convertDateToStringFormat(
                                  earliestDateCountryMap.get(partner.getCountry()))))
          .forEachOrdered(
              partner ->
                  attendeeDetails.put(
                      partner.getCountry(),
                      attendeeDetails.getOrDefault(
                          partner.getCountry(), Collections.singletonList(partner.getEmail()))));

      sendPartnerDetails(
          serviceHelper.buildAttendeeDetails(
              attendeeDetails, earliestDateCountryMap, nullCountries));

    } catch (IOException | InterruptedException | URISyntaxException exception) {
      logger.error(
          "Exception ocurred while performing the computation. Details are -"
              + exception.getStackTrace());
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Unable to fetch partner details");
    }
  }

  private void sendPartnerDetails(Countries countries)
      throws IOException, InterruptedException, URISyntaxException {

    HttpClient httpClient = HttpClient.newHttpClient();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(new URI(Endpoints.BASE_URL + Endpoints.POST_ENDPOINT))
            .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(countries)))
            .build();
    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }
}
