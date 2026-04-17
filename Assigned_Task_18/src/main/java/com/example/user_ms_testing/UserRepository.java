package com.example.user_ms_testing;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

@Repository
public class UserRepository {
    // Simulating a database with a simple list
    private List<User> database = new ArrayList<>();

    public UserRepository() {
        database.add(new User(1L, "Alice"));
        database.add(new User(2L, "Bob"));
    }

    public Optional<User> findById(Long id) {
        return database.stream().filter(u -> u.getId().equals(id)).findFirst();
    }
}