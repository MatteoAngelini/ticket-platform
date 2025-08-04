package com.platform.ticket.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        int pageSize = 10;
        Page<Ticket> ticketsPage;

        if (keyword != null && !keyword.isEmpty()) {
            ticketsPage = ticketRepository.findByTitleContainingIgnoreCase(keyword, PageRequest.of(page, pageSize));
        } else {
            ticketsPage = ticketRepository.findAll(PageRequest.of(page, pageSize));
        }

        model.addAttribute("ticketsPage", ticketsPage);
        model.addAttribute("tickets", ticketsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketsPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "/admin/index";
    }

}
