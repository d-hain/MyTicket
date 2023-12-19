package me.dave.myticket.dto;

import java.util.List;

public record CartAddTicketsDto(
    // Event id
    Long id,
    List<TicketAddDto> tickets
) {
}
