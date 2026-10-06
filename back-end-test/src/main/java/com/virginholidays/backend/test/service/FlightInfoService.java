package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * The FlightInfoService
 *
 * @author Geoff Perks
 */
public interface FlightInfoService {

    /**
     * Finds flights for any date.
     *
     * @param outboundDate the outbound departure date
     * @return optional flights.
     */
    CompletionStage<Optional<List<Flight>>> findFlightByDate(LocalDate outboundDate);
}
