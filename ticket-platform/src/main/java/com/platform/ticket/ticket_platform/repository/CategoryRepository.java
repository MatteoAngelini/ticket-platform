package com.platform.ticket.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.platform.ticket.ticket_platform.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
