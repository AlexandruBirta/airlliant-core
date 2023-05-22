package ro.unibuc.fmi.airlliantcore.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.airlliantmodel.entity.Flight;
import ro.unibuc.fmi.airlliantmodel.exception.ApiError;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Tag(name = "Flights", description = "the flights API")
@Validated
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/airlliant/v1")
public interface FlightApi {

    @Operation(summary = "Create flight", operationId = "createFlight", tags = {"Flights"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Flight already exists", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @PostMapping(value = "/flights",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    void createFlight(@Parameter(description = "Created flight object", required = true) @Valid @RequestBody Flight flight);

    @Operation(summary = "Get flight", operationId = "getFlight", tags = {"Flights"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @GetMapping(value = "/flights/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    Flight getFlight(@Parameter(description = "The flight that needs to be fetched.", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Remove flight", operationId = "removeFlight", tags = {"Flights"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "204", description = "Successful deletion. No content.")
    @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @DeleteMapping(value = "/flights/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFlight(@Parameter(description = "id of flight that needs to be removed", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Get flights", operationId = "getAllFlights", tags = {"Flights"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping(value = "/flights/all",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    List<Flight> getAllFlights();

    @Operation(summary = "Search flights", operationId = "searchFlights", tags = {"Flights"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @GetMapping(value = "/flights",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    List<Flight> searchFlights(
            @Parameter(description = "Flight from airport", required = true) @RequestParam("fromAirport") String fromAirport,
            @Parameter(description = "Flight to airport", required = true) @RequestParam("toAirport") String toAirport,
            @Parameter(description = "Flight departure", required = true) @RequestParam("departure") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departure,
            @Parameter(description = "Flight arrival", required = true) @RequestParam("arrival") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime arrival,
            @Parameter(description = "Flight round trip option", required = true) @RequestParam("roundTrip") Boolean roundTrip,
            @Parameter(description = "Flight minimum price") @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @Parameter(description = "Flight maximum price") @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice
    );

}
