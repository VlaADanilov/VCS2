package com.technokratos.vcs2.service;

import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User with username {} not found", username);
                    return new UsernameNotFoundException("User with username %s not found".formatted(username));
                });
    }
}
