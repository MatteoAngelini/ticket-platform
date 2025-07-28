package com.platform.ticket.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.platform.ticket.ticket_platform.model.Note;
import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.model.User;
import com.platform.ticket.ticket_platform.repository.NoteRepository;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import com.platform.ticket.ticket_platform.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/create/{ticketId}")
    public String create(@PathVariable("ticketId") Integer ticketId, Model model) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trovato"));
        List<User> users = userRepository.findAll();

        Note note = new Note();
        note.setCreationDate(LocalDateTime.now());

        model.addAttribute("ticket", ticket);
        model.addAttribute("users", users);
        model.addAttribute("note", note);

        return "/notes/create";
    }

    @PostMapping("/create/{ticketId}")
    public String store(@PathVariable("ticketId") Integer ticketId, @Valid @ModelAttribute("note") Note formNote,
            BindingResult bindingResult, Model model) {


        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket non trovato"));


        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("ticket", ticket);

            return "/notes/create";
        }

        formNote.setTicket(ticket);
        noteRepository.save(formNote);

        return "redirect:/tickets/" + ticketId;
    }

}
