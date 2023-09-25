package com.example.note.Controller;

import com.example.note.Config.CustomUserDetails;
import com.example.note.Config.ScheduleConfig;
import com.example.note.Model.Note;
import com.example.note.Model.Notification;
import com.example.note.Model.Tag;
import com.example.note.Model.User;
import com.example.note.Service.NoteService;
import com.example.note.Service.TagService;
import com.example.note.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/")


public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ScheduleConfig scheduleConfig;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @GetMapping("/home")
    public String index(Model model,Principal principal){
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        Long userid = userDetails.getId();
        List<Tag> tagList = tagService.alltag();
        List<Note> noteList = noteService.getAllNote(userid);
        model.addAttribute("tags",tagList);
        model.addAttribute("notes",noteList);
        return "index";
    }
    @PostMapping("/add")
    @ResponseBody
    public Map<String,Object> addnote(@RequestBody @Valid Note note,Principal principal, BindingResult bindingResult){

        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        User user = userDetails.getUser();

        note.setUser(user);
        note.setStart_at(LocalDateTime.now());
        List<String> errors = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()){
            List<ObjectError> errors1 = bindingResult.getAllErrors();
            for (ObjectError error: errors1){
                errors.add(error.getDefaultMessage());
            }
        }
        if (!errors.isEmpty()){

            response.put("errors",errors);
        }
        else {
            noteService.save(note);
            response.put("success",true);
        }
        return  response;
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id){
        noteService.deleteNote(id);
    }

    @GetMapping("/getnote/{id}")
    @ResponseBody
    public Optional<Note> getNote(@PathVariable Long id){
        return noteService.getById(id);
    }

    @PutMapping ("/update")
    @ResponseBody
    public Map<String,Object> updatenote(@RequestBody @Valid Note note,Principal principal, BindingResult bindingResult){

        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        User user = userDetails.getUser();

        note.setUser(user);
        note.setStart_at(LocalDateTime.now());
        List<String> errors = new ArrayList<>();
        Map<String,Object> response = new HashMap<>();
        if (bindingResult.hasErrors()){
            List<ObjectError> errors1 = bindingResult.getAllErrors();
            for (ObjectError error: errors1){
                errors.add(error.getDefaultMessage());
            }
        }
        if (!errors.isEmpty()){

            response.put("errors",errors);
        }
        else {
            noteService.save(note);
            response.put("success",true);
        }
        return  response;
    }
    @GetMapping("/auth")
    @ResponseBody
    public List<Note> userProfile(Model model, Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
        String username = userDetails.getUsername();
        User user = userDetails.getUser();
        List<Note> note = noteService.getAllNote(user.getId());
        // Lấy thông tin khác về người dùng từ UserDetails
        // ...

        return note;
    }
    @GetMapping("/search")
    @ResponseBody
    public List<Note> search(@RequestParam("keyword") String keyword){

        List<Note> noteList = noteService.searchNoteByTitle(keyword);
        return noteList;
    }

}

