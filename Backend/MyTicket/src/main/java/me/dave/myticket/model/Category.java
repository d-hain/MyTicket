package me.dave.myticket.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    @NonNull
    private String name;
    
    // in Euros
    @Column(name = "price")
    private int price;

    @Column(name = "stock")
    private int stock;
    
    @ManyToOne
    @NonNull
    private Event event;
    
    @OneToMany(mappedBy = "category")
    private List<Ticket> tickets;
}
