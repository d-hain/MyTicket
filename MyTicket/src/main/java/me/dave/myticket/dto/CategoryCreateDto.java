package me.dave.myticket.dto;

public record CategoryCreateDto(
    String name,
    int price,
    int stock
) {
}
