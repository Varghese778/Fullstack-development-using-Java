package com.example.user_ms_testing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserEndpoint() throws Exception {
        // Tell the fake service what to return
        when(userService.getUserGreeting(1L)).thenReturn("Hello, Alice!");

        // Simulate a GET request to /user/1 and verify status 200 OK
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Alice!"));
    }
}