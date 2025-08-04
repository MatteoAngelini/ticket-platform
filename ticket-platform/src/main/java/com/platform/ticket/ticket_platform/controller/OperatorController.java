package com.platform.ticket.ticket_platform.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.platform.ticket.ticket_platform.model.Ticket;
import com.platform.ticket.ticket_platform.model.User;
import com.platform.ticket.ticket_platform.repository.TicketRepository;
import com.platform.ticket.ticket_platform.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/operators")
public class OperatorController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public String index(Model model, Principal principal,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        int pageSize = 10;
        Page<Ticket> ticketsPage;

        if (keyword != null && !keyword.isEmpty()) {
            ticketsPage = ticketRepository.findByUser_IdAndTitleContainingIgnoreCase(user.getId(), keyword,
                    PageRequest.of(page, pageSize));
        } else {
            ticketsPage = ticketRepository.findByUser_Id(user.getId(), PageRequest.of(page, pageSize));
        }

        model.addAttribute("ticketsPage", ticketsPage);
        model.addAttribute("tickets", ticketsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketsPage.getTotalPages());
        model.addAttribute("user", user);

        return "operators/index";

    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        boolean canChangeState = !ticketRepository.existsByUser_IdAndState(user.getId(), "Da fare");

        model.addAttribute("user", user);
        model.addAttribute("canChangeState", canChangeState);
        return "/operators/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult, Model model) {

        User userId = userRepository.findById(id).orElseThrow();
        boolean canChangeState = !ticketRepository.existsByUser_IdAndState(userId.getId(), "Da fare");

        if (bindingResult.hasErrors()) {
            model.addAttribute("canChangeState", canChangeState);
            return "/operators/edit";
        }

        userId = userRepository.findById(id).orElseThrow();
        userId.setName(formUser.getName());
        userId.setSurname(formUser.getSurname());
        userId.setPassword(formUser.getPassword());
        userId.setEmail(formUser.getEmail());

        if (canChangeState) {
            userId.setState(formUser.getState());

        }

        userRepository.save(userId);

        return "redirect:/operators";
    }

}
