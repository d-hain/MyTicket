package me.dave.myticket.dto;

public record CategoryResponseDto(
    Long id,
    String name,
    int price,
    int stock
) {
}
