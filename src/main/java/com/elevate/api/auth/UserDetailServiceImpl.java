package com.elevate.api.auth;

import com.elevate.api.user.User;
import com.elevate.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return Collections.singletonList(authority);
    }
}
