package com.platform.ticket.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.platform.ticket.ticket_platform.model.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {

}
