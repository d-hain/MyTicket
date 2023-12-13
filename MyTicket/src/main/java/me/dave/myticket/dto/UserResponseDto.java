package me.dave.myticket.dto;

public record UserResponseDto(
    Long id,
    String email,
    String firstname,
    String lastname
) {
}
