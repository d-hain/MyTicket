package me.dave.myticket.service;

import me.dave.myticket.Util;
import me.dave.myticket.dto.*;
import me.dave.myticket.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CartServiceTest {
    @MockBean
    private CartRepository repository;

    @Autowired
    private CartService service;

    @Autowired
    private UserService userService;
    
    private final Util util;

    public CartServiceTest() {
        util = new Util(service, userService);
    }
    
    @Test
    void testAddTickets() {
        assert util.addTickets();
        assert repository.getReferenceById(1L).getTickets().size() == 3;
    }

    @Test
    void list() {
        util.addTickets();
        String bearerToken = util.signin();

        List<CartResponseDto> result = service.list(bearerToken);

        assert result.size() == 1;
        assert result.get(0).name().equals("Luffys Birthday");
    }

    @Test
    void checkout() {
        util.addTickets();
        String bearerToken = util.signin();

        CartCheckoutDto result = service.checkout(bearerToken);

        assert result.price() == 100_400;
    }
}
