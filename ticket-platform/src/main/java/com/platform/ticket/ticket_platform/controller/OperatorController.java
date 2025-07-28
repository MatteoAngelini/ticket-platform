package com.platform.ticket.ticket_platform.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.model.User;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import com.platform.ticket.ticket_platform.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/operators")
public class OperatorController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @GetMapping
    public String show(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        List<Ticket> tickets = ticketRepository.findByUser_Id(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("tickets", tickets);
        
        return "operators/show";

    }

}
