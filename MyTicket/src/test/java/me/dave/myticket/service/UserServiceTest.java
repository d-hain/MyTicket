package me.dave.myticket.service;

import me.dave.myticket.dto.UserSigninDto;
import me.dave.myticket.dto.UserSignupDto;
import me.dave.myticket.model.User;
import me.dave.myticket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {
    @MockBean
    private UserRepository repository;

    @Autowired
    private UserService service;
    
    @Test
    void signup() {
        User user = new User();
        user.setEmail("gojo@godaddy.com");
        user.setFirstname("Gojo");
        user.setLastname("Satoru");
        user.setPassword("TheStrongest");

        UserSignupDto userDto = new UserSignupDto(
            user.getEmail(), 
            user.getFirstname(),
            user.getLastname(),
            user.getPassword()
        );
        String bearerToken = service.signup(userDto);
        
        assert bearerToken != null;
        assert repository.getReferenceById(3L).getEmail().equals(user.getEmail());
    }
    
    @Test
    void signin() {
        User user = new User();
        user.setEmail("monkey.d.luffy@straw.hat");
        user.setPassword("MEAT");
        
        UserSigninDto userDto = new UserSigninDto(
            user.getEmail(),
            user.getPassword()
        );
        service.signin(userDto);
        
        Mockito.verify(repository, Mockito.times(1)).findByEmail(user.getEmail());
    }
}
