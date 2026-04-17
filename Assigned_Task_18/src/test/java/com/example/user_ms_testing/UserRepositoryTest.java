package com.example.user_ms_testing;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepository();

    @Test
    public void testFindById_UserExists() {
        Optional<User> user = userRepository.findById(1L);
        assertTrue(user.isPresent());
        assertEquals("Alice", user.get().getName());
    }

    @Test
    public void testFindById_UserDoesNotExist() {
        Optional<User> user = userRepository.findById(99L);
        assertFalse(user.isPresent()); // Proves it handles bad IDs correctly
    }
}