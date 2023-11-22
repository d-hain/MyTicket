package me.dave.myticket.dto;

public record TicketResponseDto(
    Long id,
    
    // Category name
    String name,
    
    int price,
    int amount
) {
    public TicketResponseDto incrementAmount() {
        return new TicketResponseDto(
            this.id(),
            this.name(),
            this.price(),
            this.amount() + 1
        );
    }
}
