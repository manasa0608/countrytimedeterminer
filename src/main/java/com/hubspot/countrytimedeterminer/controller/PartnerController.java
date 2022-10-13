package com.hubspot.countrytimedeterminer.controller;

import com.hubspot.countrytimedeterminer.resources.Partners;
import com.hubspot.countrytimedeterminer.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PartnerController {

  @Autowired PartnerService partnerService;

  /**
   * Get method to fetch all the partner details
   *
   * @return
   */
  @GetMapping("/partner")
  public Partners getPartnerInformation() {
    return partnerService.getPartnerDetails();
  }

  /**
   * Post method to determine attendee details as per their available date and country
   *
   * @throws IOException
   * @throws InterruptedException
   */
  @PostMapping("/partner")
  public void sendPartnerAvailability() throws IOException, InterruptedException {
    partnerService.determinePartnerDatesAvailability();
  }
}
