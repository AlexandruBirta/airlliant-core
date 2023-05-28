package ro.unibuc.fmi.airlliantcore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;

import java.util.List;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsTicketByFlightAndSeatRowAndSeatNumber(Flight flight, String seatRow, String seatNumber);

    List<Ticket> findAllByUserEmail(String email);

}
