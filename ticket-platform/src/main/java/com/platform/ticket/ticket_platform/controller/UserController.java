package com.platform.ticket.ticket_platform.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.platform.ticket.ticket_platform.model.Role;
import com.platform.ticket.ticket_platform.model.User;
import com.platform.ticket.ticket_platform.repository.RoleRepository;
import com.platform.ticket.ticket_platform.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String index(Model model, @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page) {

        int pageSize = 10;
        Page<User> usersPage;

        if (keyword != null && !keyword.isEmpty()) {
            usersPage = userRepository.findByNameContainingIgnoreCase(keyword, PageRequest.of(page, pageSize));
        } else {
            usersPage = userRepository.findAll(PageRequest.of(page, pageSize));
        }

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "users/index";
    }

    @GetMapping("/create")
    public String create(Model model) {

        List<Role> roles = roleRepository.findAll();

        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "users/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("user") User formUser, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "users/create";
        }

        Set<Role> selectedRole = formUser.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Ruolo non valido")))
                .collect(Collectors.toSet());

        formUser.setName(formUser.getName());
        formUser.setSurname(formUser.getSurname());
        formUser.setEmail(formUser.getEmail());
        formUser.setState(formUser.getState());
        formUser.setRoles(selectedRole);
        formUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        userRepository.save(formUser);

        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {

        Optional<User> user = userRepository.findById(id);
        List<Role> roles = roleRepository.findAll();

        if (user.isEmpty()) {
            return "redirect:/error/404";
        }

        model.addAttribute("user", user.get());
        model.addAttribute("roles", roles);
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("user") User formUser,
            BindingResult bindingResult, Model model) {

        User user = userRepository.findById(id).orElseThrow();

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleRepository.findAll());
            return "/users/edit";
        }

        Set<Role> selectedRole = formUser.getRoles().stream()
                .map(role -> roleRepository.findById(role.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Ruolo non valido")))
                .collect(Collectors.toSet());

        user.setName(formUser.getName());
        user.setSurname(formUser.getSurname());
        user.setEmail(formUser.getEmail());
        user.setPassword(passwordEncoder.encode(formUser.getPassword()));
        user.setState(formUser.getState());
        user.setRoles(selectedRole);

        userRepository.save(user);

        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model) {

        userRepository.deleteById(id);
        return "redirect:/users";
    }

}
