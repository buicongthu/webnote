package com.example.note.Config;

import com.example.note.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về danh sách các quyền (roles) của người dùng, ví dụ ADMIN, USER, ...
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }

    @Override
    public String getPassword() {
        // Trả về mật khẩu của người dùng
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // Trả về tên đăng nhập của người dùng
        return user.getName();
    }
    public Long getId(){
        return user.getId();
    }

    public User getUser() {
        return user;
    }

    // Các phương thức khác cần được triển khai tương ứng
    // Như isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
