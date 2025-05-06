package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.NotFoundException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.exception.registration.EmailExistsEsception;
import com.technokratos.vcs2.exception.registration.UsernameExistsException;
import com.technokratos.vcs2.model.Role;
import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import com.technokratos.vcs2.model.dto.response.UserResponseDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReportService reportService;

    public UUID saveUser(@Valid RegisterUserDto regDto) {
        log.info("Registering new user with username: {}", regDto.getUsername());

        if (userRepository.findByUsername(regDto.getUsername()).isPresent()) {
            log.warn("Username {} already exists", regDto.getUsername());
            throw new UsernameExistsException(regDto.getUsername());
        }

        if (userRepository.findByEmail(regDto.getEmail()).isPresent()) {
            log.warn("Email {} already exists", regDto.getEmail());
            throw new EmailExistsEsception(regDto.getEmail());
        }

        User user = new User(regDto.getUsername(), regDto.getPassword());
        user.setEmail(regDto.getEmail());
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_DEFAULT.toString());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        return savedUser.getId();
    }

    public User findUserByCarId(UUID car_id) {
        log.debug("Finding user associated with car ID: {}", car_id);

        return userRepository.findByAuto(car_id)
                .orElseThrow(() -> {
                    log.warn("No user found for car ID: {}", car_id);
                    return new NotFoundException("User not found in car with id %s".formatted(car_id), "/");
                });
    }

    public List<UserResponseDto> getAllUsers(int page, int size) {
        log.info("Fetching all users (page: {}, size: {})", page, size);

        Page<User> all = userRepository.findAll(PageRequest.of(page, size));
        List<UserResponseDto> responseDtos = all.stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();

        log.info("Found {} users", responseDtos.size());
        return responseDtos;
    }

    public void doModerator(UUID userId) {
        log.info("Promoting user {} to moderator role", userId);

        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            throw new UserNotFoundException(userId);
        } else {
            User user = byId.get();
            user.setRole(Role.ROLE_MODERATOR.toString());
            userRepository.save(user);
            log.info("User {} is now a moderator", userId);
        }
    }

    public void doDefault(UUID userId) {
        log.info("Reverting user {} to default role", userId);

        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            log.warn("User with ID {} not found", userId);
            throw new UserNotFoundException(userId);
        } else {
            User user = byId.get();
            user.setRole(Role.ROLE_DEFAULT.toString());
            reportService.deleteAllViewWhere(user.getId());
            userRepository.save(user);
            log.info("User {} is now a default user", userId);
        }
    }

    public User findByUsername(String username) {
        log.debug("Fetching user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User with username {} not found", username);
                    return new UserNotFoundException(username);
                });
    }

    public Long countOfAllUserPages() {
        long totalUsers = userRepository.count();
        Long totalPages = totalUsers % 10 == 0 ? totalUsers / 10 : totalUsers / 10 + 1;

        log.info("Total user pages: {}", totalPages);
        return totalPages;
    }

    public List<UserResponseDto> searchUsers(String search, int page, int size) {
        log.info("Searching users by username starting with '{}'", search);

        List<UserResponseDto> responseDtos = userRepository.findByUsernameStartingWithIgnoreCase(search, PageRequest.of(page, size))
                .stream()
                .map(user -> new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();

        log.info("Found {} users matching search term: {}", responseDtos.size(), search);
        return responseDtos;
    }

    public Long countOfAllUserPages(String search) {
        Long totalUsers = userRepository.countByUsernameStartingWithIgnoreCase(search);
        if (totalUsers == 0) {
            totalUsers = 1L;
        }

        Long totalPages = totalUsers % 10 == 0 ? totalUsers / 10 : totalUsers / 10 + 1;
        log.info("Total pages for search '{}': {}", search, totalPages);
        return totalPages;
    }

    public void checkForExist(UUID userId) {
        log.debug("Checking existence of user ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            log.warn("User with ID {} does not exist", userId);
            throw new UserNotFoundException(userId);
        }
    }
}
