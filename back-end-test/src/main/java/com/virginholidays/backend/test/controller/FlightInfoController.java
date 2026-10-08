package com.virginholidays.backend.test.controller;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletionStage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.CacheControl.noCache;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api")
public class FlightInfoController {

    private final FlightInfoService flightInfoService;

    public FlightInfoController(FlightInfoService flightInfoService) {
        this.flightInfoService = flightInfoService;
    }

    @GetMapping(path = "/flights/{date}", produces = "application/json")
    public CompletionStage<ResponseEntity<?>> getFlightsByDate(@PathVariable String date) {
        return getFlightsByDateQuery(date);
    }

    /**
     * Alternative endpoint that accepts the date as a query parameter: /api/flights?date=yyyy-MM-dd
     */
    @GetMapping(path = "/flights", produces = "application/json")
    public CompletionStage<ResponseEntity<?>> getFlightsByDateQuery(String date) {
        LocalDate outboundDate;
        try {
            outboundDate = LocalDate.parse(date);
        } catch (Exception e) {
            // return 400 Bad Request for invalid date format
            return java.util.concurrent.CompletableFuture.completedFuture(
                status(HttpStatus.BAD_REQUEST).body("Invalid date format. Expected yyyy-MM-dd")
            );
        }

        return flightInfoService.findFlightByDate(outboundDate).thenApply(maybeFlights -> {
            if (maybeFlights.isEmpty()) {
                return status(HttpStatus.NO_CONTENT).cacheControl(noCache()).build();
            }

            List<Flight> flights = maybeFlights.get();
            return status(HttpStatus.OK).cacheControl(noCache()).body(flights);
        });
    }
}
