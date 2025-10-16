package ru.arapov.somerestjpafitches.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.arapov.somerestjpafitches.dtos.UserDto;
import ru.arapov.somerestjpafitches.exceptions.UserExceptions;
import ru.arapov.somerestjpafitches.models.User;
import ru.arapov.somerestjpafitches.repos.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUser(Long userId) {
        User user =  userRepository.findUserById(userId).orElseThrow(() -> new UserExceptions("User not found with id " + userId));
        return UserDto.from(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto createUser(String username, String email, String encodedPassword) {
        User user = new User();

        if (userRepository.existsByUsername(username)) {
            throw new UserExceptions("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserExceptions("Email already exists");
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        return UserDto.from(savedUser);
    }
}
