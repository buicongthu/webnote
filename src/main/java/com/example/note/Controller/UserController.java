package com.example.note.Controller;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.example.note.Model.User;
import com.example.note.Repository.UserRepository;
import com.example.note.Service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/Note")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String viewSignup(){
        return "signup";
    }
    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("user") User user,  @RequestParam("passwordconfirm") String passwordconfirm, Model model, BindingResult bindingResult) {

        List<String> errors = new ArrayList<>();

        if(bindingResult.hasErrors()){
            List<ObjectError> ERRORS = bindingResult.getAllErrors();
            for (ObjectError error: ERRORS){
                errors.add(error.getDefaultMessage());
            }
        }

        String formemail = user.getEmail();
        if((userService.findbyEmail(formemail))!=null){
            errors.add("gmail nay da duoc su dung");
        }

        if(!user.getPassword().equals(passwordconfirm)){
            errors.add("mat khau xac nhan khong khop");
        }
        if(!errors.isEmpty()){
            model.addAttribute("errors",errors);
            return "signup";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/Note/login";


    }

    @GetMapping ("/login")
    public String viewLogin(){
        return "login";
    }


}
