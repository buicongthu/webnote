package com.example.note.Service;

import com.example.note.Model.Tag;
import com.example.note.Repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    public void save(Tag tag){
        tagRepository.save(tag);
    }
    public List<Tag> alltag(){
        return tagRepository.findAll();
    }
    public Optional<Tag> getByid(Long id){
        return tagRepository.findById(id);
    }
    public void deletetag(Long id){
        tagRepository.deleteById(id);
    }
}
