package me.dave.myticket.service;

import me.dave.myticket.dto.*;
import me.dave.myticket.model.*;
import me.dave.myticket.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    private final CartRepository repository;
    private final UserService userService;
    private final EventService eventService;
    private final TicketService ticketService;

    public CartService(
        CartRepository repository,
        UserService userService,
        EventService eventService,
        TicketService ticketService
    ) {
        this.repository = repository;
        this.userService = userService;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    /**
     * @param bearerToken The users token with the "Bearer " prefix
     */
    public boolean addTickets(String bearerToken, CartAddTicketsDto ticketDtos) {
        if (bearerToken == null || bearerToken.isEmpty() || ticketDtos == null) {
            return false;
        }

        String token = bearerToken.split(" ")[1];
        User user = userService.getByToken(token);
        if (user == null) {
            return false;
        }

        // create cart if user does not already have one
        Cart cart = repository.findByUser(user).orElse(null);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            cart = repository.save(newCart);

            user.setCart(cart);
            user = userService.save(user);
        }

        List<Ticket> ticketsList = cart.getTickets();

        Event event = eventService.getById(ticketDtos.id());
        if (event == null) {
            return false;
        }

        for (TicketAddDto ticketDto : ticketDtos.tickets()) {
            // check if tickets exists
            Ticket ticket = ticketService.getById(ticketDto.id());
            if (ticket == null) {
                return false;
            }

            // add the ticket to the cart if enough tickets are available
            Category category = ticket.getCategory();
            if (ticketDto.amount() <= category.getStock()) {
                category.setStock(category.getStock() - ticketDto.amount());
                ticketService.save(ticket);

                for (int i = 0; i < ticketDto.amount(); i++) {
                    ticketsList.add(ticket);
                }
            } else {
                return false;
            }
        }

        cart.setTickets(ticketsList);

        repository.save(cart);

        return true;
    }

    /**
     * @param bearerToken The users token with the "Bearer " prefix
     */
    public List<CartResponseDto> list(String bearerToken) {
        if (bearerToken == null || bearerToken.isEmpty()) {
            return null;
        }

        Cart cart = userService.getByToken(bearerToken.split(" ")[1]).getCart();

        List<Event> events = new ArrayList<>();
        // Get all events from the cart
        for (Ticket ticket : cart.getTickets()) {
            Event event = ticket.getCategory().getEvent();
            if (!events.contains(event)) {
                events.add(event);
            }
        }

        // NOTE: This has to be one of the most beautiful code snippets of all time
        List<CartResponseDto> cartResponse = new ArrayList<>();
        for (Event event : events) {
            // Assign all tickets to their event
            List<TicketResponseDto> ticketDtos = new ArrayList<>();
            for (Ticket ticket : cart.getTickets()) {
                Category category = ticket.getCategory();
                if (category.getEvent().equals(event)) {
                    // this list should always only contain one ticket
                    List<TicketResponseDto> existingTickets = ticketDtos.stream()
                        .filter(t -> t.id().equals(ticket.getId()))
                        .toList();
                    if (existingTickets.size() > 1) {
                        System.err.println("CartService: Ticket is in cart multiple times!");
                        return null;
                    }

                    // Increment amount if ticket already exists
                    if (!existingTickets.isEmpty() && existingTickets.get(0) != null) {
                        for (TicketResponseDto t : ticketDtos) {
                            if (t.id().equals(ticket.getId())) {
                                int existingTicketIndex = ticketDtos.indexOf(existingTickets.get(0));
                                ticketDtos.set(existingTicketIndex, existingTickets.get(0).incrementAmount());
                            }
                        }
                    } else {
                        TicketResponseDto ticketDto = new TicketResponseDto(
                            ticket.getId(),
                            category.getName(),
                            category.getPrice(),
                            1
                        );

                        ticketDtos.add(ticketDto);
                    }
                }
            }

            cartResponse.add(new CartResponseDto(
                event.getId(),
                event.getName(),
                event.getFrom(),
                event.getTo(),
                event.getDescription(),
                ticketDtos
            ));
        }

        return cartResponse;
    }

    /**
     * @param bearerToken The users token with the "Bearer " prefix
     */
    public CartCheckoutDto checkout(String bearerToken) {
        if (bearerToken == null || bearerToken.isEmpty()) {
            return null;
        }

        Cart cart = userService.getByToken(bearerToken.split(" ")[1]).getCart();

        CartCheckoutDto cartCheckout = new CartCheckoutDto(0);
        for (Ticket ticket : cart.getTickets()) {
            cartCheckout = cartCheckout.addPrice(ticket.getCategory().getPrice());
        }

        return cartCheckout;
    }
}
