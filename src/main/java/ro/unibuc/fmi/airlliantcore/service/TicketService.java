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
    public void createTicket(Ticket ticket, Long userId, Long flightId) {

        int min = 1;
        int max = 100;

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionStatus.USER_NOT_FOUND, String.valueOf(userId)));
        Flight flight = flightRepository.findById(flightId).orElseThrow(() -> new ApiException(ExceptionStatus.FLIGHT_NOT_FOUND, String.valueOf(flightId)));
        String seatRow = ticket.getSeatRow();
        String seatNumber = ticket.getSeatNumber();

        if (ticketRepository.existsTicketByFlightAndSeatRowAndSeatNumber(flight, seatRow, seatNumber)) {
            throw new ApiException(ExceptionStatus.TICKET_ALREADY_EXISTS, String.valueOf(userId), String.valueOf(flightId), seatRow, seatNumber);
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

}