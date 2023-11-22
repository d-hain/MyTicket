package me.dave.myticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.dave.myticket.dto.EventCreateDto;
import me.dave.myticket.dto.EventResponseDto;
import me.dave.myticket.dto.EventUpdateDto;
import me.dave.myticket.permission.Permissions;
import me.dave.myticket.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }
    
    @Operation(
        summary = "Create a new event",
        description = "This can only be done by an admin",
        tags = { "event", "create" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to create the event"
        )
    })
    @PostMapping("/create")
    @Permissions(user = false)
    public ResponseEntity<Void> createEvent(
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody EventCreateDto event
    ) {
        if (service.create(event)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Update an event",
        description = "This can only be done by an admin",
        tags = { "event", "update" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to update the event"
        )
    })
    @PutMapping("/update")
    @Permissions(user = false)
    public ResponseEntity<Void> updateEvent(
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody EventUpdateDto event
    ) {
        if (service.update(event)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Delete an event",
        description = "This can only be done by an admin",
        tags = { "event", "delete" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to delete the event"
        )
    })
    @DeleteMapping("/{id}")
    @Permissions(user = false)
    public ResponseEntity<Void> deleteEvent(
        @RequestHeader("Authorization") String bearerToken,
        @PathVariable Long id
    ) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Load an event",
        description = "This can be done by everyone excluding guests",
        tags = { "event", "load" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to delete the event"
        )
    })
    @GetMapping("/{id}")
    @Permissions(user = true)
    public ResponseEntity<EventResponseDto> loadEvent(
        @RequestHeader("Authorization") String bearerToken,
        @PathVariable Long id
    ) {
        EventResponseDto event = service.load(id);
        if (event != null) {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "List all events",
        description = "This can be done by everyone excluding guests",
        tags = { "event", "list" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = EventResponseDto.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to delete the event"
        )
    })
    @GetMapping("/list")
    @Permissions(user = true)
    public ResponseEntity<List<EventResponseDto>> listEvents(
        @RequestHeader("Authorization") String bearerToken
    ){
        List<EventResponseDto> events = service.list();
        if (events != null) {
            return new ResponseEntity<>(events, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
