package me.dave.myticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record EventCreateDto(
    String name,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm") Date from,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm") Date to,
    String description,
    List<CategoryCreateDto> ticketCategories
) {
}
