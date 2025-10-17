package ru.arapov.somerestjpafitches.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.arapov.somerestjpafitches.dtos.RegisterRequest;
import ru.arapov.somerestjpafitches.dtos.UserDto;
import ru.arapov.somerestjpafitches.models.User;
import ru.arapov.somerestjpafitches.repos.UserRepository;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() {
        // Given
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password123");

        // When
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                request,
                UserDto.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUsername()).isEqualTo("testuser");
        assertThat(response.getBody().getEmail()).isEqualTo("test@test.com");

        User savedUser = userRepository.findByUsername("testuser").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
        assertThat(passwordEncoder.matches("password123", savedUser.getPassword())).isTrue();
    }

    @Test
    void shouldLoginUser() throws Exception {
        // Given
        User user = new User();
        user.setUsername("loginuser");
        user.setEmail("login@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        userRepository.save(user);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "loginuser")
                        .param("password", "password123"))
                .andExpect(status().isOk());
    }
}