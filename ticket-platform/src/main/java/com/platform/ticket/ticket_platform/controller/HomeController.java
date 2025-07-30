package com.platform.ticket.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.ticket.ticket_platform.repository.NoteRepository;
import com.platform.ticket.ticket_platform.repository.TicketRepository;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/home")
    public String index(Model model) {

        long ticketCount = ticketRepository.count();
        long noteCount = noteRepository.count();

        model.addAttribute("ticketCount", ticketCount);
        model.addAttribute("noteCount", noteCount);
        
        return "home/index";

    }

    @GetMapping
    public String login() {
        return "home/login";
    }
    
}
