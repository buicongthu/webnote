package com.example.note.Repository;

import com.example.note.Model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {

    List<Note> findByUserId(Long id);

    List<Note> findByTitle(String keyword);
}
