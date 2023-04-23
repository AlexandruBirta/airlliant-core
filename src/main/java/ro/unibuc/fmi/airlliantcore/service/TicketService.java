package ro.unibuc.fmi.airlliantcore.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.unibuc.fmi.airlliantcore.repository.TicketRepository;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;
import ro.unibuc.fmi.airlliantmodel.exception.ApiException;
import ro.unibuc.fmi.airlliantmodel.exception.ExceptionStatus;


@Slf4j
@Service
@AllArgsConstructor
public class TicketService {


    private final TicketRepository ticketRepository;

    @Transactional
    public void createTicket(Ticket ticket) {

        Long id = ticket.getId();

        if (ticketRepository.existsById(id)) {
            throw new ApiException(ExceptionStatus.TICKET_ALREADY_EXISTS, String.valueOf(id));
        }

        ticketRepository.save(ticket);
        log.info("Created ticket: {}", ticket);

    }

    @Transactional
    public void removeTicket(Long id) {

        if (!ticketRepository.existsById(id)) {
            throw new ApiException(ExceptionStatus.TICKET_NOT_FOUND, String.valueOf(id));
        }

        ticketRepository.deleteById(id);

        log.info("Deleted ticket with id '{}'.", id);

    }

    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> new ApiException(ExceptionStatus.TICKET_NOT_FOUND, String.valueOf(id)));
    }

}