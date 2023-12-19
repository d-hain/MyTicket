package me.dave.myticket;

import me.dave.myticket.dto.CartAddTicketsDto;
import me.dave.myticket.dto.TicketAddDto;
import me.dave.myticket.dto.UserSigninDto;
import me.dave.myticket.service.CartService;
import me.dave.myticket.service.UserService;

import java.util.List;

public class Util {
    private final CartService cartService;
    private final UserService userService;

    public Util(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    public String signin() {
        UserSigninDto userDto = new UserSigninDto(
            "monkey.d.luffy@straw.hat",
            "MEAT"
        );
        return userService.signin(userDto);
    }

    public boolean addTickets() {
        String bearerToken = this.signin();

        List<TicketAddDto> ticketAddDtos = List.of(
            new TicketAddDto(2L, 2),
            new TicketAddDto(3L, 1)
        );
        CartAddTicketsDto ticketDtos = new CartAddTicketsDto(
            1L,
            ticketAddDtos
        );

        return cartService.addTickets(bearerToken, ticketDtos);
    }
}
