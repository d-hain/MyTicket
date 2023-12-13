package me.dave.myticket.dto;

public record UserUpdateDto(
    Long id,
    String email,
    String firstname,
    String lastname
) {
}
