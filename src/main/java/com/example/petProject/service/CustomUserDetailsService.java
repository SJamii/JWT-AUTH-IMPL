package com.example.petProject.service;

import com.example.petProject.entity.User;
import com.example.petProject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isEnabled = true;
        User user = userRepository.findByUsernameIgnoreCaseAndEnabled(username, isEnabled);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
