package com.platform.ticket.ticket_platform.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;


import com.platform.ticket.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    
    Optional<User> findByEmail(String email);

    List<User> findByRoles_Name(String roleName);

}
