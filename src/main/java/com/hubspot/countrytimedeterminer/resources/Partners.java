package com.hubspot.countrytimedeterminer.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Partners {
    private List<Partner> partners;
}

