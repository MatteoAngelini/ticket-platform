package com.platform.ticket.ticket_platform.repository;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.ticket.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Integer>{

    List<Ticket> findByUser_Id(Integer userId);
    Page<Ticket> findByUser_Id(Integer userId, Pageable pageable);
    Page<Ticket> findByUser_IdAndTitleContainingIgnoreCase(Integer userId, String keyword, Pageable pageable);
    
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);

    Page<Ticket> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    boolean existsByUser_IdAndState(Integer userId, String state);
}
