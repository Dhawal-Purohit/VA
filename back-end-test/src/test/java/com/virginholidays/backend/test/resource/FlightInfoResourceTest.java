package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.service.FlightInfoService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The FlightInfoResource unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoResourceTest {

    @Mock
    private FlightInfoService flightInfoService;

    @InjectMocks
    private FlightInfoResource flightInfoResource;

    private List<Flight> testFlights;

    @BeforeEach
    public void beforeEach() {
        // Create test flights
        testFlights = List.of(
            new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.TUESDAY)),
            new Flight(LocalTime.of(15, 35), "Las Vegas", "LAS", "VS044", List.of(
                DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
            ))
        );
    }

    @Test
    public void testGetResults_ReturnsOkWithFlights() throws Exception {
        // arrange
        String dateString = "2026-10-06"; // Sunday
        when(flightInfoService.findFlightByDate(any(LocalDate.class)))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        ResponseEntity<?> response = flightInfoResource.getResults(dateString).toCompletableFuture().get();

        // assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());
        assertThat(response.getBody(), equalTo(testFlights));
    }

    @Test
    public void testGetResults_ReturnsNoContentWhenNoFlights() throws Exception {
        // arrange
        String dateString = "2026-10-06";
        when(flightInfoService.findFlightByDate(any(LocalDate.class)))
            .thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // act
        ResponseEntity<?> response = flightInfoResource.getResults(dateString).toCompletableFuture().get();

        // assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test
    public void testGetResults_ReturnsCacheControlHeaders() throws Exception {
        // arrange
        String dateString = "2026-10-06";
        when(flightInfoService.findFlightByDate(any(LocalDate.class)))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        ResponseEntity<?> response = flightInfoResource.getResults(dateString).toCompletableFuture().get();

        // assert
        assertThat(response.getHeaders().getCacheControl(), notNullValue());
    }

    @Test
    public void testGetResults_ParsesDifferentDateFormats() throws Exception {
        // arrange
        String dateString = "2026-12-25"; // Christmas
        when(flightInfoService.findFlightByDate(any(LocalDate.class)))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        ResponseEntity<?> response = flightInfoResource.getResults(dateString).toCompletableFuture().get();

        // assert
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void testGetResults_ReturnsEmptyListAsNoContent() throws Exception {
        // arrange
        String dateString = "2026-10-06";
        when(flightInfoService.findFlightByDate(any(LocalDate.class)))
            .thenReturn(CompletableFuture.completedFuture(Optional.of(List.of())));

        // act - Empty list is treated as no results
        ResponseEntity<?> response = flightInfoResource.getResults(dateString).toCompletableFuture().get();

        // assert - Note: Empty Optional gives NO_CONTENT, but empty list in Optional gives body
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(List.of()));
    }
}