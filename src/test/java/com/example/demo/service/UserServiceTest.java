package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_shouldReturnSavedUser() {
        User user = new User("amir", "amir@gmail.com");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("amir");
        verify(userRepository).save(user);
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll())
                .thenReturn(List.of(new User("amir", "amir@gmail.com")));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(1);
    }

    @Test
    void getUserById_whenExists_shouldReturnUser() {
        User user = new User("amir", "amir@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isPresent();
    }

    @Test
    void getUserById_whenNotExists_shouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void updateUser_shouldUpdateFields() {
        User existing = new User("old", "old@mail.com");
        User updated = new User("new", "new@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenReturn(existing);

        Optional<User> result = userService.updateUser(1L, updated);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("new");
    }

    @Test
    void deleteUser_shouldCallRepository() {
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}
