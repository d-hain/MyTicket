package me.dave.myticket.dto;

public record CategoryUpdateDto(
    Long id,
    String name,
    int price,
    int stock
) {
}
