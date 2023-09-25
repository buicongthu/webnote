package com.example.note.Model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    public enum TaskStatus {
        TODO,
        DOING,
        DONE
    }
    private TaskStatus status;
    private LocalDateTime start_at;
    private LocalDateTime end_at;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
