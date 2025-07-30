package com.platform.ticket.ticket_platform.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {
    
    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public  ResponseEntity<List<Ticket>> getAllTickets(){
        return ResponseEntity.ok(ticketRepository.findAll());
    }
    
    @GetMapping(params = "category")
    public ResponseEntity<List<Ticket>> getTicketsByCategory(@RequestParam("category") Integer categoryId){
        List<Ticket> tickets = ticketRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping(params = "state")
    public ResponseEntity<List<Ticket>> getTicketsByState(@RequestParam("state") String state){
        List<Ticket> tickets = ticketRepository.findByState(state);
        return ResponseEntity.ok(tickets);
    }

}
