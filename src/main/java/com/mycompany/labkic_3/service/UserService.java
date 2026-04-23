package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.entity.AppUser;
import com.mycompany.labkic_3.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void register(String username, String rawPassword) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(passwordEncoder.encode(rawPassword));
        appUser.setRole("ROLE_USER");
        userRepository.save(appUser);
    }
}
