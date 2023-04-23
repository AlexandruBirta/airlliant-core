package ro.unibuc.fmi.airlliantcore.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.unibuc.fmi.airlliantcore.api.TicketApi;
import ro.unibuc.fmi.airlliantcore.service.TicketService;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class TicketController implements TicketApi {

    private final TicketService ticketService;

    @Override
    public void createTicket(@Valid @RequestBody Ticket ticket) {
        ticketService.createTicket(ticket);
    }

    @Override
    public void deleteTicket(@PathVariable("id") Long id) {
        ticketService.removeTicket(id);
    }

    @Override
    public Ticket getTicket(@PathVariable("id") Long id) {
        return ticketService.getTicket(id);
    }

}