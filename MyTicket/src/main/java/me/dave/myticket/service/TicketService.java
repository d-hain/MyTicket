package me.dave.myticket.service;

import me.dave.myticket.model.Ticket;
import me.dave.myticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository repository;
    
    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }
     
    public Ticket getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public Ticket save(Ticket ticket) {
        return repository.save(ticket);
    }
}
