package com.example.note.Config;

import com.example.note.Model.User;
import com.example.note.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        // Lấy thông tin người dùng từ cơ sở dữ liệu hoặc nguồn dữ liệu khác
        User user = userRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        }
        // Tạo UserDetails từ thông tin người dùng và trả về
        return  new CustomUserDetails(user);
    }
}
