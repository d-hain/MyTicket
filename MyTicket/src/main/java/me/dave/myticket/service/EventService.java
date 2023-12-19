package me.dave.myticket.service;

import me.dave.myticket.dto.*;
import me.dave.myticket.model.Category;
import me.dave.myticket.model.Event;
import me.dave.myticket.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {
    private final EventRepository repository;
    private final CategoryService categoryService;

    public EventService(EventRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    public static EventResponseDto map(Event in) {
        if (in == null) {
            return null;
        }

        return new EventResponseDto(
            in.getId(),
            in.getName(),
            in.getFrom(),
            in.getTo(),
            in.getDescription(),
            in.getTicketCategories().stream()
                .map(CategoryService::map)
                .toList()
        );
    }

    public Event getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public boolean create(EventCreateDto event) {
        Event newEvent = new Event();

        newEvent.setName(event.name());
        newEvent.setFrom(event.from());
        newEvent.setTo(event.to());
        newEvent.setDescription(event.description());

        List<Category> categories = new ArrayList<>();
        for (CategoryCreateDto categoryDto : event.ticketCategories()) {
            Category category = CategoryService.map(categoryDto);
            categories.add(category);
            categoryService.save(category);
        }
        newEvent.setTicketCategories(categories);
        
        repository.save(newEvent);

        return true;
    }

    public boolean update(EventUpdateDto newEvent) {
        Event event = repository.findById(newEvent.id()).orElse(null);
        if (event == null) {
            return false;
        }

        event.setName(newEvent.name());
        event.setFrom(newEvent.from());
        event.setTo(newEvent.to());
        event.setDescription(newEvent.description());

        List<Category> categories = event.getTicketCategories();
        List<CategoryUpdateDto> matchingCategories = newEvent.ticketCategories().stream()
            .filter(category -> categories.stream().anyMatch(c -> c.getId().equals(category.id())))
            .toList();

        List<Category> newCategories = new ArrayList<>();
        for (CategoryUpdateDto category : matchingCategories) {
            newCategories.add(CategoryService.map(category));
        }
        event.setTicketCategories(newCategories);
        
        repository.save(event);

        return true;
    }

    public EventResponseDto load(Long id) {
        return repository.findById(id).map(EventService::map).orElse(null);
    }
    
    public List<EventResponseDto> listFutureEvents() {
        return repository.findEventsByFromAfter(new Date()).stream().map(EventService::map).toList();
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
