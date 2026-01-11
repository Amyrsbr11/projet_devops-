package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= CREATE =================
    @Test
    void createUser_shouldReturn200() throws Exception {
        User user = new User("amir", "amir@gmail.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("amir"));
    }

    // ================= GET ALL =================
    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(new User("amir", "amir@gmail.com")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("amir"));
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ================= GET BY ID =================
    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(userService.getUserById(1L))
                .thenReturn(Optional.of(new User("amir", "amir@gmail.com")));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("amir"));
    }

    @Test
    void getUserById_shouldReturn404() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    // ================= UPDATE =================
    @Test
    void updateUser_shouldReturnUser() throws Exception {
        User user = new User("updated", "updated@gmail.com");

        when(userService.updateUser(eq(1L), any(User.class)))
                .thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/users/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    void updateUser_shouldReturn404() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/1")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ================= DELETE =================
    @Test
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
