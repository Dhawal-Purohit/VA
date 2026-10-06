package com.virginholidays.backend.test.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

/**
 * The flight record
 *
 * @author Geoff Perks
 */
public record Flight (

    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("departureTime")
    LocalTime departureTime,

    @JsonProperty("destination")
    String destination,

    @JsonProperty("iata")
    String iata,

    @JsonProperty("flightNo")
    String flightNo,

    @JsonProperty("days")
    List<DayOfWeek> days

) { }
