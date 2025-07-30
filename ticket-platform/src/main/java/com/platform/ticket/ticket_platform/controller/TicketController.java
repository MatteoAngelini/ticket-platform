package com.platform.ticket.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.model.User;
import com.platform.ticket.ticket_platform.repository.CategoryRepository;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import com.platform.ticket.ticket_platform.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket non trovato con id: " + id));
      

        model.addAttribute("notes", ticket.getNotes());
        model.addAttribute("ticket", ticket);

        return "/tickets/show";
    }

    @GetMapping("/create")
    public String create(Model model) {

        List<User> operators = userRepository.findByRoleAnState("Operatore", "Disponibile");

        Ticket ticket = new Ticket();

        ticket.setCreationDate(LocalDateTime.now());
        

        model.addAttribute("ticket", ticket);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("user", userRepository.findAll());
        model.addAttribute("operators", operators);

        return "/tickets/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket formTicket, BindingResult bindingResult, Model model) {

        List<User> operators = userRepository.findByRoleAnState("Operatore", "Disponibile");

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("user", userRepository.findAll());
            model.addAttribute("operators", operators);

            return "/tickets/create";
        }

        formTicket.setUpdateDate(LocalDateTime.now());
        ticketRepository.save(formTicket);

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {

        List<User> operators = userRepository.findByRoleAnState("Operatore", "Disponibile");

        Optional<Ticket> ticket = ticketRepository.findById(id);

        model.addAttribute("ticket", ticket.get());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("user", userRepository.findAll());
        model.addAttribute("operators", operators);

        return "/tickets/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket formTicket,
            BindingResult bindingResult, Model model, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isOperator = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("Operatore"));

        Ticket ticketId = ticketRepository.findById(id).orElseThrow();

        if (isOperator) {
            ticketId.setState(formTicket.getState());
            ticketId.setUpdateDate(LocalDateTime.now());
            ticketRepository.save(ticketId);
            return "redirect:/operators";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("user", userRepository.findAll());
            model.addAttribute("operators", userRepository.findByRoleAnState("Operatore", "Disponibile"));

            return "/tickets/edit";
        }

        
        ticketId.setTitle(formTicket.getTitle());
        ticketId.setDescription(formTicket.getDescription());
        ticketId.setCategory(formTicket.getCategory());
        ticketId.setUser(formTicket.getUser());
        ticketId.setState(formTicket.getState());
        ticketId.setUpdateDate(LocalDateTime.now());

        ticketRepository.save(ticketId);

        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id")Integer id, Model model) {
        
        ticketRepository.deleteById(id);
        return "redirect:/admin";
    }
    

}
