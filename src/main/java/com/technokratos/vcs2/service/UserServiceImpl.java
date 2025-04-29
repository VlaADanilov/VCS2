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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Validated
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UUID saveUser(@Valid RegisterUserDto regDto) {
        if (userRepository.findByUsername(regDto.getUsername()).isPresent()) {
            throw new UsernameExistsException(regDto.getUsername());
        }
        if (userRepository.findByEmail(regDto.getEmail()).isPresent()) {
            throw new EmailExistsEsception(regDto.getEmail());
        }
        User user = new User(regDto.getUsername(), regDto.getPassword());
        user.setEmail(regDto.getEmail());
        user.setId(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_DEFAULT.toString());
        return userRepository.save(user).getId();
    }

    public User findUserByCarId(UUID car_id) {
        return userRepository.findByAuto(car_id).orElseThrow(
                () ->
                        new NotFoundException("User not found in car with id %s".formatted(car_id), "/"));
    }

    public List<UserResponseDto> getAllUsers(int page, int size) {
        Page<User> all = userRepository.findAll(PageRequest.of(page, size));
        return all.stream().map(user -> new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        )).toList();
    }

    public void doModerator(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new UserNotFoundException(userId);
        } else {
            byId.get().setRole(Role.ROLE_MODERATOR.toString());
            userRepository.save(byId.get());
        }
    }

    public void doDefault(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isEmpty()) {
            throw new UserNotFoundException(userId);
        } else {
            byId.get().setRole(Role.ROLE_DEFAULT.toString());
            userRepository.save(byId.get());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}
