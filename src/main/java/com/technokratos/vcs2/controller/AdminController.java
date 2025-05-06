package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.AdminApi;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController implements AdminApi {
    private final UserServiceImpl userService;

    public void doModerator(UUID userId) {
        log.info("Do moderato user with id {}", userId);
        userService.doModerator(userId);
    }

    public void doDefault(UUID userId) {
        log.info("Do default user with id {}", userId);
        userService.doDefault(userId);
    }
}
