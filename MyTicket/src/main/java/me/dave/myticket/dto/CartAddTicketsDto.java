package me.dave.myticket.dto;

import java.util.List;

public record CartAddTicketsDto(
    // The id of the event
    Long id,
    List<TicketAddDto> tickets
) {
}
