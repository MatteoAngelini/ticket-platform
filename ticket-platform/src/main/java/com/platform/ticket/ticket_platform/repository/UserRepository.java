package com.platform.ticket.ticket_platform.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.platform.ticket.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :role AND u.state = :state")
    List<User> findByRoleAnState(@Param("role") String role, @Param("state") String state);

}
