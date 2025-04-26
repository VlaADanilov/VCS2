package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.EmailExistsEsception;
import com.technokratos.vcs2.exception.UsernameExistsException;
import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
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
        user.setRole("DEFAULT");
        return userRepository.save(user).getId();
    }
}
