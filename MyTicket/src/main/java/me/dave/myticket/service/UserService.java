package me.dave.myticket.service;

import me.dave.myticket.dto.UserResponseDto;
import me.dave.myticket.dto.UserSigninDto;
import me.dave.myticket.dto.UserSignupDto;
import me.dave.myticket.dto.UserUpdateDto;
import me.dave.myticket.model.Role;
import me.dave.myticket.model.User;
import me.dave.myticket.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public static UserResponseDto map(User in) {
        if (in == null) {
            return null;
        }

        return new UserResponseDto(
            in.getId(),
            in.getEmail(),
            in.getFirstname(),
            in.getLastname()
        );
    }

    private String generateToken(String userEmail) {
        return passwordEncoder.encode(userEmail);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * @param bearerToken The users token with the "Bearer " prefix
     */
    public boolean isValidToken(String bearerToken, Role role) {
        if (bearerToken == null || bearerToken.isEmpty()) {
            return false;
        }
        System.err.println(bearerToken);
        String token = bearerToken.split(" ")[1];

        Optional<User> user = repository.findUserByTokenAndTokenExpirationIsBeforeAndRole(
            token,
            new Date(),
            role
        );
        
        return user.isPresent();
    }
    
    public User save(User user) {
        return repository.save(user);
    }
    
    public User getByToken(String token) {
        return repository.findByToken(token).orElse(null);
    }
    
    /**
     * @return Bearer token if successful, null if failed
     */
    public String signup(UserSignupDto user) {
        // User already exists
        if (repository.findByEmail(user.email()).isPresent()) {
            return null;
        }

        User newUser = new User();
        newUser.setEmail(user.email());
        newUser.setFirstname(user.firstname());
        newUser.setLastname(user.lastname());
        newUser.setPassword(this.encodePassword(user.password()));

        String token = generateToken(newUser.getEmail());
        newUser.setToken(token);

        // Token expiration is 10 minutes
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        Date expiration = cal.getTime();
        newUser.setTokenExpiration(expiration);

        repository.save(newUser);

        return token;
    }

    /**
     * @return Bearer token if successful, null if failed
     */
    public String signin(UserSigninDto signinDto) {
        Optional<User> result = repository.findByEmail(signinDto.email());
        // User does not exist
        if (result.isEmpty()) {
            return null;
        }
        User user = result.get();
        
        // Passwords do not match
        if (passwordEncoder.matches(user.getPassword(), signinDto.password())) {
            return null;
        }

        String token = generateToken(user.getEmail());
        user.setToken(token);

        // Token expiration is 10 minutes
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 10);
        Date expiration = cal.getTime();
        user.setTokenExpiration(expiration);

        repository.save(user);

        return token;
    }

    public boolean update(UserUpdateDto newUser) {
        User user = repository.findById(newUser.id()).orElse(null);
        if (user == null) {
            return false;
        }

        user.setEmail(newUser.email());
        user.setFirstname(newUser.firstname());
        user.setLastname(newUser.lastname());

        repository.save(user);

        return true;
    }

    public List<UserResponseDto> list() {
        return repository.findAll().stream()
            .map(UserService::map)
            .toList();
    }

    public UserResponseDto load(Long id) {
        return map(repository.findById(id).orElse(null));
    }

    public boolean delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
