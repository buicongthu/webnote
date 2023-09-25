package com.example.note.Service;

import com.example.note.Model.Note;
import com.example.note.Repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;
    public void save(Note note){
        noteRepository.save(note);
    }
    public void deleteNote(Long id){
        noteRepository.deleteById(id);
    }
    public List<Note> allNote(){
        return noteRepository.findAll();
    }
    public List<Note> getAllNote(Long id){
        return noteRepository.findByUserId(id);
    }
    public Optional<Note> getById(Long id){
        return noteRepository.findById(id);
    }

    public List<Note> searchNoteByTitle(String keyword) {
        return noteRepository.findByTitle(keyword);
    }
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }


}
