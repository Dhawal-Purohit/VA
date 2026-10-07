package com.virginholidays.backend.test.service;

import com.virginholidays.backend.test.api.Flight;
import com.virginholidays.backend.test.repository.FlightInfoRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The FlightInfoServiceImpl unit tests
 *
 * @author Geoff Perks
 */
@ExtendWith(MockitoExtension.class)
class FlightInfoServiceImplTest {

    @Mock
    private FlightInfoRepository flightInfoRepository;

    @InjectMocks
    private FlightInfoServiceImpl flightInfoService;

    private List<Flight> testFlights;

    @BeforeEach
    public void beforeEach() {
        // Create test flights with different day-of-week schedules
        testFlights = List.of(
            // Flight 1: Only operates on Tuesdays
            new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.TUESDAY)),
            // Flight 2: Operates all 7 days
            new Flight(LocalTime.of(15, 35), "Las Vegas", "LAS", "VS044", List.of(
                DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
            )),
            // Flight 3: Only operates on weekends
            new Flight(LocalTime.of(11, 5), "Barbados", "BGI", "VS029", List.of(
                DayOfWeek.SUNDAY, DayOfWeek.SATURDAY
            )),
            // Flight 4: Only operates on weekdays
            new Flight(LocalTime.of(12, 0), "Cancun", "CUN", "VS093", List.of(
                DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
            ))
        );
    }

    @Test
    public void testFindFlightByDate_SundayReturnsWeekendAndAllDaysFlights() throws Exception {
        // arrange - October 4, 2026 is a Sunday
        LocalDate sunday = LocalDate.of(2026, 10, 4);
        when(flightInfoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        Optional<List<Flight>> result = flightInfoService.findFlightByDate(sunday).toCompletableFuture().get();

        // assert - Should return flights that operate on Sunday
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), hasSize(2)); // Las Vegas (all days) and Barbados (weekends)
        assertThat(result.get().get(0).flightNo(), equalTo("VS044")); // Las Vegas
        assertThat(result.get().get(1).flightNo(), equalTo("VS029")); // Barbados
    }

    @Test
    public void testFindFlightByDate_TuesdayReturnsAllMatchingFlights() throws Exception {
        // arrange - October 6, 2026 is a Tuesday
        LocalDate tuesday = LocalDate.of(2026, 10, 6);
        when(flightInfoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        Optional<List<Flight>> result = flightInfoService.findFlightByDate(tuesday).toCompletableFuture().get();

        // assert - Should return all flights that operate on Tuesday
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), hasSize(3)); // Antigua, Las Vegas, and Cancun
    }

    @Test
    public void testFindFlightByDate_WednesdayReturnsWeekdayFlights() throws Exception {
        // arrange - October 7, 2026 is a Wednesday
        LocalDate wednesday = LocalDate.of(2026, 10, 7);
        when(flightInfoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Optional.of(testFlights)));

        // act
        Optional<List<Flight>> result = flightInfoService.findFlightByDate(wednesday).toCompletableFuture().get();

        // assert - Should return flights for weekday
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), hasSize(2)); // Las Vegas (all days) and Cancun (weekdays)
    }

    @Test
    public void testFindFlightByDate_RepositoryReturnsEmpty() throws Exception {
        // arrange - October 4, 2026 is a Sunday
        LocalDate testDate = LocalDate.of(2026, 10, 4);
        when(flightInfoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        // act
        Optional<List<Flight>> result = flightInfoService.findFlightByDate(testDate).toCompletableFuture().get();

        // assert - Should return empty Optional
        assertThat(result.isPresent(), equalTo(false));
    }

    @Test
    public void testFindFlightByDate_NoFlightsOperateOnGivenDay() throws Exception {
        // arrange - Create flights that don't operate on Monday; October 5, 2026 is a Monday
        List<Flight> limitedFlights = List.of(
            new Flight(LocalTime.of(9, 0), "Antigua", "ANU", "VS033", List.of(DayOfWeek.TUESDAY)),
            new Flight(LocalTime.of(11, 5), "Barbados", "BGI", "VS029", List.of(DayOfWeek.SUNDAY, DayOfWeek.SATURDAY))
        );
        LocalDate monday = LocalDate.of(2026, 10, 5);
        when(flightInfoRepository.findAll()).thenReturn(CompletableFuture.completedFuture(Optional.of(limitedFlights)));

        // act
        Optional<List<Flight>> result = flightInfoService.findFlightByDate(monday).toCompletableFuture().get();

        // assert - Should return empty list for a day with no flights
        assertThat(result.isPresent(), equalTo(true));
        assertThat(result.get(), hasSize(0));
    }
}