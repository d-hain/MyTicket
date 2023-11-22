package me.dave.myticket.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @NonNull
    private Category category;
    
    @ManyToMany(mappedBy = "tickets")
    private List<Cart> carts;
}
