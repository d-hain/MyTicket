package me.dave.myticket.dto;

public record CartCheckoutDto(
    int price
) {
    public CartCheckoutDto addPrice(int price) {
        return new CartCheckoutDto(this.price + price);
    }
}
