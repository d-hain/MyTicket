package me.dave.myticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import me.dave.myticket.dto.CartAddTicketsDto;
import me.dave.myticket.dto.CartCheckoutDto;
import me.dave.myticket.dto.CartResponseDto;
import me.dave.myticket.permission.Permissions;
import me.dave.myticket.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService service;
    
    public CartController(CartService service) {
        this.service = service;
    }
    
    @Operation(
        summary = "Add tickets to cart",
        description = "This can be done by everyone excluding guests",
        tags = { "cart", "add", "ticket" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successful operation"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to add tickets to cart"
        )
    })   
    @PostMapping("/add")
    @Permissions(user = true)
    public ResponseEntity<Void> addTicketsToCart(
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody CartAddTicketsDto cartAddTickets
    ) {
        if (service.addTickets(bearerToken, cartAddTickets)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "List tickets in cart",
        description = "This can be done by everyone excluding guests",
        tags = { "cart", "list", "ticket" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = CartResponseDto.class)))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to list tickets in cart"
        )
    })
    @GetMapping("/list")
    @Permissions(user = true)
    public ResponseEntity<List<CartResponseDto>> listCart(
        @RequestHeader("Authorization") String bearerToken
    ) {
        List<CartResponseDto> cart = service.list(bearerToken);
        if (cart != null) {
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Operation(
        summary = "Checkout and get total price",
        description = "This can be done by everyone excluding guests",
        tags = { "cart", "checkout" }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Successful operation",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CartCheckoutDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Failed to checkout cart"
        )
    })
    @PostMapping("/checkout")
    @Permissions(user = true)
    public ResponseEntity<CartCheckoutDto> checkoutCart(
        @RequestHeader("Authorization") String bearerToken
    ) {
        CartCheckoutDto cartCheckout = service.checkout(bearerToken);
        if (cartCheckout != null) {
            return new ResponseEntity<>(cartCheckout, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
