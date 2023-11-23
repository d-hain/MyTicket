package me.dave.myticket.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "first_name")
    @NonNull
    private String firstname;

    @Column(name = "last_name")
    @NonNull
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "token_expiration")
    private Date tokenExpiration;
    
    @OneToOne
    private Cart cart;
}
