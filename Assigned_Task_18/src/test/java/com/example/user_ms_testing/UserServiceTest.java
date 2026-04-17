package com.example.user_ms_testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserGreeting_Success() {
        // Arrange: Tell the fake database what to return
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "TestUser")));

        // Act: Run the logic
        String result = userService.getUserGreeting(1L);

        // Assert: Verify it formatted the greeting correctly
        assertEquals("Hello, TestUser!", result);
    }
}