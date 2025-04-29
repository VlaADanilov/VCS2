package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;

    @PostMapping("/{user_id}/doModerator")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void doModerator(@PathVariable("user_id")UUID userId) {
        userService.doModerator(userId);
    }

    @PostMapping("/{user_id}/doUser")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void doDefault(@PathVariable("user_id")UUID userId) {
        userService.doDefault(userId);
    }
}
