package me.dave.myticket.controller;

import me.dave.myticket.Util;
import me.dave.myticket.service.CartService;
import me.dave.myticket.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    
    @MockBean
    private CartService service;

    private final Util util;
    
    public CartControllerTest() {
        util = new Util(service, userService);
    }
    
    @Test
    void addTicketsToCart() throws Exception {
        mockMvc.perform(
            post("/api/v1/cart/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                    """
                        {
                           "id": 2,
                              "tickets": [
                                 {
                                    "id": 4,
                                    "amount": 1
                                 },
                                 {
                                    "id": 6,
                                    "amount": 2
                                 }
                              ]
                        }
                        """
                )
        ).andExpect(status().isOk());
    }

    @Test
    void listCart() throws Exception {
        mockMvc.perform(
                get("/api/v1/cart/list").contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Luffys Birthday"));
    }
    
    @Test
    void checkoutCart() throws Exception {
        util.addTickets();       
        
        mockMvc.perform(
                post("/api/v1/cart/checkout").contentType(MediaType.APPLICATION_JSON_VALUE)
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.price").value(100_400));
    }
}
