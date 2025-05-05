package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.SecurityApi;
import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import com.technokratos.vcs2.model.form.LoginForm;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class SecurityController implements SecurityApi {
    private final UserServiceImpl userService;


    public String login(String error,
                        Model model
    ) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }
        model.addAttribute("loginForm", new LoginForm());
        return "security/login";
    }


    public String registration(Model model) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        model.addAttribute("regDto", registerUserDto);
        return "security/registration";
    }


    public void addUser(RegisterUserDto regDto) {
        userService.saveUser(regDto);
    }
}
