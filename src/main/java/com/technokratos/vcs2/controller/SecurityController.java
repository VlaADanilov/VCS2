package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import com.technokratos.vcs2.model.form.LoginForm;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserServiceImpl userService;

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model
    ) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        model.addAttribute("loginForm", new LoginForm());
        return "security/login";
    }

    @GetMapping("/registration")
    @PreAuthorize("isAnonymous()")
    public String registration(Model model) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        model.addAttribute("regDto", registerUserDto);
        return "security/registration";
    }

    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody RegisterUserDto regDto,
                          Model model) {
        userService.saveUser(regDto);
    }
}
