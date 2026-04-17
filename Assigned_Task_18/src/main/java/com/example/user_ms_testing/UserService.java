package com.example.user_ms_testing;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserGreeting(Long id) {
        return userRepository.findById(id)
                .map(user -> "Hello, " + user.getName() + "!")
                .orElse("User not found");
    }
}