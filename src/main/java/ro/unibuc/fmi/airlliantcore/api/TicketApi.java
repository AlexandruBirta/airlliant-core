package ro.unibuc.fmi.airlliantcore.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.fmi.airlliantmodel.entity.Ticket;
import ro.unibuc.fmi.airlliantmodel.exception.ApiError;

import javax.validation.Valid;


@Tag(name = "Tickets", description = "the tickets API")
@Validated
@RequestMapping(value = "/airlliant/v1")
public interface TicketApi {

    @Operation(summary = "Create ticket", operationId = "createTicket", tags = {"Tickets"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Ticket already exists", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @PostMapping(value = "/tickets",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    void createTicket(@Parameter(description = "Created ticket object", required = true) @Valid @RequestBody Ticket ticket);

    @Operation(summary = "Get ticket", operationId = "getTicket", tags = {"Tickets"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @GetMapping(value = "/tickets/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    Ticket getTicket(@Parameter(description = "The ticket that needs to be fetched.", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Remove ticket", operationId = "removeTicket", tags = {"Tickets"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "204", description = "Successful deletion. No content.")
    @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @DeleteMapping(value = "/tickets/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTicket(@Parameter(description = "id of ticket that needs to be removed", required = true) @PathVariable("id") Long id);


}