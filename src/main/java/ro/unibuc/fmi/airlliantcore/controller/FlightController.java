package ro.unibuc.fmi.airlliantcore.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.airlliantcore.api.FlightApi;
import ro.unibuc.fmi.airlliantcore.service.FlightService;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@AllArgsConstructor
public class FlightController implements FlightApi {

    private final FlightService flightService;

    @Override
    public void createFlight(@Valid @RequestBody Flight flight) {
        flightService.createFlight(flight);
    }

    @Override
    public void deleteFlight(@PathVariable("id") Long id) {
        flightService.removeFlight(id);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @Override
    public List<Flight> searchFlights(
            String fromAirport,
            String toAirport,
            LocalDateTime departure,
            LocalDateTime arrival,
            Boolean roundTrip,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return flightService.searchFlights(
                fromAirport,
                toAirport,
                departure,
                arrival,
                roundTrip,
                minPrice,
                maxPrice
        );
    }

    @Override
    public Flight getFlight(@PathVariable("id") Long id) {
        return flightService.getFlight(id);
    }

}