package ro.unibuc.fmi.airlliantcore.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.airlliantcore.repository.FlightRepository;
import ro.unibuc.fmi.airlliantcore.repository.TicketRepository;
import ro.unibuc.fmi.airlliantcore.repository.UserRepository;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;
import ro.unibuc.fmi.airlliantmodel.entity.User;
import ro.unibuc.fmi.airlliantmodel.exception.ApiException;
import ro.unibuc.fmi.airlliantmodel.exception.ExceptionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@Service
@AllArgsConstructor
public class TicketService {


    private final AirlliantQuartzService airlliantQuartzService;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public void createTicket(Ticket ticket, String email, Long flightId) {

        int min = 1;
        int max = 100;

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(ExceptionStatus.USER_NOT_FOUND_BY_EMAIL, email));
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new ApiException(ExceptionStatus.FLIGHT_NOT_FOUND, String.valueOf(flightId)));
        String seatRow = ticket.getSeatRow();
        String seatNumber = ticket.getSeatNumber();

        if (flight.getDepartureDate().isBefore(LocalDateTime.now())) {
            throw new ApiException(ExceptionStatus.FLIGHT_ALREADY_DEPARTED, String.valueOf(flightId), flight.getDepartureDate().toString());
        }

        if (ticketRepository.existsTicketByFlightAndSeatRowAndSeatNumber(flight, seatRow, seatNumber)) {
            throw new ApiException(ExceptionStatus.TICKET_ALREADY_EXISTS_FOR_EMAIL, email, String.valueOf(flightId), seatRow, seatNumber);
        }

        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setGate("G" + (ThreadLocalRandom.current().nextInt(min, max)));

        airlliantQuartzService.addTicket(ticket);

    }

    @Transactional
    public void removeTicket(Long id) {

        if (!ticketRepository.existsById(id)) {
            throw new ApiException(ExceptionStatus.TICKET_NOT_FOUND, String.valueOf(id));
        }

        airlliantQuartzService.deleteTicket(id);

    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionStatus.TICKET_NOT_FOUND, String.valueOf(id)));
    }

    public List<Ticket> getUserTickets(String email) {
        return ticketRepository.findAllByUserEmail(email);
    }

}