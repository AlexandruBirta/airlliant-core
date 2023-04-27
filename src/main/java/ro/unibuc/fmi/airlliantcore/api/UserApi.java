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
import ro.unibuc.fmi.airlliantmodel.entity.User;
import ro.unibuc.fmi.airlliantmodel.exception.ApiError;

import javax.validation.Valid;


@Tag(name = "Users", description = "the users API")
@Validated
@RequestMapping(value = "/airlliant/v1")
public interface UserApi {

    @Operation(summary = "Create user", operationId = "createUser", tags = {"Users"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "User already exists", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @PostMapping(value = "/users",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Parameter(description = "Created user object", required = true) @Valid @RequestBody User user);

    @Operation(summary = "Get user", operationId = "getUser", tags = {"Users"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @GetMapping(value = "/users/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    User getUser(@Parameter(description = "The user that needs to be fetched.", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Remove user", operationId = "removeUser", tags = {"Users"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "204", description = "Successful deletion. No content.")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @DeleteMapping(value = "/users/{id}",
            produces = {"application/json"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@Parameter(description = "ID of user that needs to be removed", required = true) @PathVariable("id") Long id);

    @Operation(summary = "Create ticket", operationId = "createTicket", tags = {"Tickets", "Users"})
    @Parameter(name = "Correlation-Id", description = "Correlation-Id for logging purposes", in = ParameterIn.HEADER, schema = @Schema(type = "string", format = "uuid"))
    @Parameter(name = "Origin-Application-Name", description = "Application of origin", in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponse(responseCode = "201", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Ticket already exists", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ApiError.class))))
    @PostMapping(value = "/users/{userId}/flights/{flightId}/tickets",
            produces = {"application/json"},
            consumes = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    void createTicket(@Parameter(description = "Created ticket object", required = true) @Valid @RequestBody Ticket ticket,
                      @Parameter(description = "ID of user", required = true) @PathVariable("userId") Long userId,
                      @Parameter(description = "ID of flight", required = true) @PathVariable("flightId") Long flightId
    );


}
