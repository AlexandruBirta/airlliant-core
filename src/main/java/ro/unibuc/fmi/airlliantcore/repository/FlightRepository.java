package ro.unibuc.fmi.airlliantcore.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceBetween(
            @NotNull(message = "FromAirport must not be null.") @Size(min = 3, max = 3) String fromAirport,
            @NotNull(message = "ToAirport must not be null.") @Size(min = 3, max = 3) String toAirport,
            @NotNull(message = "Departure date must not be null.") LocalDateTime departureDate,
            @NotNull(message = "Arrival date must not be null.") LocalDateTime arrivalDate,
            @NotNull(message = "Round trip must not be null.") Boolean roundTrip,
            @NotNull(message = "Price must not be null.") @DecimalMin(value = "1.00", message = "Balance value must be higher or equal than 1.00.") BigDecimal minPrice,
            @NotNull(message = "Price must not be null.") @DecimalMin(value = "1.00", message = "Balance value must be higher or equal than 1.00.") BigDecimal maxPrice
    );

    List<Flight> findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceIsGreaterThanEqual(
            @NotNull(message = "FromAirport must not be null.") @Size(min = 3, max = 3) String fromAirport,
            @NotNull(message = "ToAirport must not be null.") @Size(min = 3, max = 3) String toAirport,
            @NotNull(message = "Departure date must not be null.") LocalDateTime departureDate,
            @NotNull(message = "Arrival date must not be null.") LocalDateTime arrivalDate,
            @NotNull(message = "Round trip must not be null.") Boolean roundTrip,
            @NotNull(message = "Price must not be null.") @DecimalMin(value = "1.00", message = "Balance value must be higher or equal than 1.00.") BigDecimal minPrice
    );

    List<Flight> findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTripAndPriceIsLessThanEqual(
            @NotNull(message = "FromAirport must not be null.") @Size(min = 3, max = 3) String fromAirport,
            @NotNull(message = "ToAirport must not be null.") @Size(min = 3, max = 3) String toAirport,
            @NotNull(message = "Departure date must not be null.") LocalDateTime departureDate,
            @NotNull(message = "Arrival date must not be null.") LocalDateTime arrivalDate,
            @NotNull(message = "Round trip must not be null.") Boolean roundTrip,
            @NotNull(message = "Price must not be null.") @DecimalMin(value = "1.00", message = "Balance value must be higher or equal than 1.00.") BigDecimal maxPrice
    );

    List<Flight> findAllByFromAirportAndToAirportAndDepartureDateAfterAndArrivalDateBeforeAndRoundTrip(
            @NotNull(message = "FromAirport must not be null.") @Size(min = 3, max = 3) String fromAirport,
            @NotNull(message = "ToAirport must not be null.") @Size(min = 3, max = 3) String toAirport,
            @NotNull(message = "Departure date must not be null.") LocalDateTime departureDate,
            @NotNull(message = "Arrival date must not be null.") LocalDateTime arrivalDate,
            @NotNull(message = "Round trip must not be null.") Boolean roundTrip
    );


}
