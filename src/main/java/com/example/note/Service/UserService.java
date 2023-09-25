package com.example.note.Service;

import com.example.note.Model.User;
import com.example.note.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User findbyEmail(String gmail){
        return userRepository.findByEmail( gmail);
    }

    public void save(User user){
        userRepository.save(user);
    }
    public List<User> findall()
    {
        return userRepository.findAll();
    }

//    public User findByUsername(String name){
//        return userRepository.findByUsername(name);
//    }

}
