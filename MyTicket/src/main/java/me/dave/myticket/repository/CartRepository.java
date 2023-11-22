package me.dave.myticket.repository;

import me.dave.myticket.model.Cart;
import me.dave.myticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
