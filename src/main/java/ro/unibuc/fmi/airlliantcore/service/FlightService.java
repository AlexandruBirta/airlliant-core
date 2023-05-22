package ro.unibuc.fmi.airlliantcore.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.airlliantcore.repository.FlightRepository;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;
import ro.unibuc.fmi.airlliantmodel.exception.ApiException;
import ro.unibuc.fmi.airlliantmodel.exception.ExceptionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class FlightService {


    private final FlightRepository flightRepository;

    @Transactional
    public void createFlight(Flight flight) {

        flightRepository.save(flight);
        log.info("Created flight: {}", flight);

        String flightNumber = flight.getFromAirport() + flight.getToAirport() + flight.getId();
        flight.setFlightNumber(flightNumber);

    }

    @Transactional
    public void removeFlight(Long id) {

        if (!flightRepository.existsById(id)) {
            throw new ApiException(ExceptionStatus.FLIGHT_NOT_FOUND, String.valueOf(id));
        }

        flightRepository.deleteById(id);

        log.info("Deleted flight with id '{}'.", id);

    }

    public Flight getFlight(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionStatus.FLIGHT_NOT_FOUND, String.valueOf(id)));
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> searchFlights(
            String fromAirport,
            String toAirport,
            LocalDateTime departure,
            LocalDateTime arrival,
            Boolean roundTrip,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {

        departure = departure.minusDays(1);
        arrival = arrival.plusDays(1);

        if (minPrice != null && maxPrice != null) {
            return flightRepository.findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceBetween(
                    fromAirport,
                    toAirport,
                    departure,
                    arrival,
                    roundTrip,
                    minPrice,
                    maxPrice
            );
        }

        if (minPrice != null && maxPrice == null) {
            return flightRepository.findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceIsGreaterThanEqual(
                    fromAirport,
                    toAirport,
                    departure,
                    arrival,
                    roundTrip,
                    minPrice
            );
        }

        if (minPrice == null && maxPrice != null) {
            return flightRepository.findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceIsLessThanEqual(
                    fromAirport,
                    toAirport,
                    departure,
                    arrival,
                    roundTrip,
                    maxPrice
            );
        }

        if (minPrice == null && maxPrice == null) {
            return flightRepository.findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTrip(
                    fromAirport,
                    toAirport,
                    departure,
                    arrival,
                    roundTrip
            );
        }

        return Collections.emptyList();

    }

}