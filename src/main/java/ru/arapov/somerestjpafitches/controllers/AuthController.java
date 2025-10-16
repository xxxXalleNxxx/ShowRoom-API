package ru.arapov.somerestjpafitches.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.arapov.somerestjpafitches.dtos.RegisterRequest;
import ru.arapov.somerestjpafitches.dtos.UserDto;
import ru.arapov.somerestjpafitches.models.CustomUserDetails;
import ru.arapov.somerestjpafitches.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        UserDto user = userService.createUser(
                request.username(),
                request.email(),
                encodedPassword
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDto user = userService.getUser(userDetails.getUser().getId());
        return ResponseEntity.ok(user);
    }
}
