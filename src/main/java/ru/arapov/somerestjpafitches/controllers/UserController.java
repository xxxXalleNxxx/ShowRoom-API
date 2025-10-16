package ru.arapov.somerestjpafitches.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.arapov.somerestjpafitches.dtos.UserDto;
import ru.arapov.somerestjpafitches.services.UserService;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }
}
