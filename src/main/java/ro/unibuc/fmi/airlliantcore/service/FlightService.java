package ro.unibuc.fmi.airlliantcore.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.airlliantcore.repository.FlightRepository;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;
import ro.unibuc.fmi.airlliantmodel.exception.ApiException;
import ro.unibuc.fmi.airlliantmodel.exception.ExceptionStatus;

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

}