package com.platform.ticket.ticket_platform.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.platform.ticket.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Integer>{

    List<Ticket> findByUser_Id(Integer userId);
    
    List<Ticket> findByTitleContainingIgnoreCase(String keyword);
}
