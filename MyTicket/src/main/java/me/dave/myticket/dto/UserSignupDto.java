package me.dave.myticket.dto;

public record UserSignupDto(
    String email,
    String firstname,
    String lastname,
    String password
) {
}
