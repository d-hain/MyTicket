package me.dave.myticket.dto;

import java.util.Date;
import java.util.List;

public record CartResponseDto(
    // Event
    Long id,
    String name,
    Date from,
    Date to,
    String description,
    List<TicketResponseDto> tickets
) {
}
