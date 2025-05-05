package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.AdminApi;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AdminController implements AdminApi {
    private final UserServiceImpl userService;

    public void doModerator(UUID userId) {
        userService.doModerator(userId);
    }

    public void doDefault(UUID userId) {
        userService.doDefault(userId);
    }
}
