package me.dave.myticket.service;

import me.dave.myticket.dto.CategoryCreateDto;
import me.dave.myticket.dto.CategoryUpdateDto;
import me.dave.myticket.dto.EventCreateDto;
import me.dave.myticket.dto.EventUpdateDto;
import me.dave.myticket.model.Category;
import me.dave.myticket.model.Event;
import me.dave.myticket.model.Ticket;
import me.dave.myticket.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class EventServiceTest {
    @MockBean
    private EventRepository repository;

    @Autowired
    private EventService service;
    
    @Test
    void create() throws Exception {
        Event event = new Event();
        event.setName("Chapter 236");
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        event.setFrom(dateFormat.parse("24.09.2023 12:00"));
        event.setTo(dateFormat.parse("24.09.2023 13:00"));
        event.setDescription("Mowed");
        
        Category category1 = new Category();
        category1.setEvent(event);
        category1.setName("Free");
        category1.setStock(100);
        category1.setPrice(0);

        List<Ticket> tickets1 = new ArrayList<>();
        for (int i = 0; i < category1.getStock(); i++) {
            Ticket ticket = new Ticket();
            ticket.setCategory(category1);
            tickets1.add(ticket);
        }
        category1.setTickets(tickets1);
        
        Category category2 = new Category();
        category2.setEvent(event);
        category2.setName("VIP");
        category2.setStock(50);
        category2.setPrice(0);

        List<Ticket> tickets2 = new ArrayList<>();
        for (int i = 0; i < category2.getStock(); i++) {
            Ticket ticket = new Ticket();
            ticket.setCategory(category2);
            tickets2.add(ticket);
        }
        category2.setTickets(tickets2);
        event.setTicketCategories(List.of(category1, category2));

        List<CategoryCreateDto> ticketCategories = new ArrayList<>();
        for (Category category : event.getTicketCategories()) {
            CategoryCreateDto categoryDto = new CategoryCreateDto(
                category.getName(),
                category.getStock(),
                category.getPrice()
            );
            ticketCategories.add(categoryDto);
        }
        
        EventCreateDto eventDto = new EventCreateDto(
            event.getName(),
            event.getFrom(),
            event.getTo(),
            event.getDescription(),
            ticketCategories
        );
        boolean result = service.create(eventDto);

        assert result;
        assert repository.getReferenceById(3L).getDescription().equals(event.getDescription());
    }
    
    @Test
    void update() {
        Event event = repository.getReferenceById(2L);
        
        List<CategoryUpdateDto> ticketCategories = new ArrayList<>();
        for (Category category : event.getTicketCategories()) {
            ticketCategories.add(CategoryService.map(CategoryService.map(category)));
        }
        
        EventUpdateDto eventDto = new EventUpdateDto(
            2L,
            event.getName(),
            event.getFrom(),
            event.getTo(),
            event.getDescription() + "!",
            ticketCategories   
        );
        boolean result = service.update(eventDto);
        
        assert result;
        assert repository.getReferenceById(2L).getDescription().equals(event.getDescription());
    }
}
