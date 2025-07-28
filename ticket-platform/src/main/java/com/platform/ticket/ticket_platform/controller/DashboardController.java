package com.platform.ticket.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.repository.TicketRepository;

import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/")
public class DashboardController {

    @Autowired
    private TicketRepository ticketRepository;


    @GetMapping
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        
        List<Ticket> tickets;

        if (keyword != null && !keyword.isEmpty()) {
            tickets = ticketRepository.findByTitleContainingIgnoreCase(keyword);
        }else{
            tickets = ticketRepository.findAll();
        }
        
        model.addAttribute("tickets", tickets);
        model.addAttribute("keyword", keyword);

        return "/dashboard/index";
    }
    
    
}
