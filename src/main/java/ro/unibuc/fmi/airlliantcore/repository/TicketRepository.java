package ro.unibuc.fmi.airlliantcore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
