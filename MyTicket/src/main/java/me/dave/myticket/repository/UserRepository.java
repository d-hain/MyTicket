package me.dave.myticket.repository;

import me.dave.myticket.model.Role;
import me.dave.myticket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByTokenAndTokenExpirationIsBeforeAndRole(
        String token,
        Date tokenExpirationBefore,
        Role role
    );
}
