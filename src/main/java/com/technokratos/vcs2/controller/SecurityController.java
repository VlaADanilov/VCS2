package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.request.RegisterUserDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.model.form.LoginForm;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SecurityController {
    private final UserServiceImpl userService;

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @GetMapping("/registration")
    @PreAuthorize("isAnonymous()")
    public String registration(Model model) {
        RegisterUserDto registerUserDto = new RegisterUserDto();
        model.addAttribute("regDto", registerUserDto);
        return "registration";
    }

    @PostMapping("/registration")
    @PreAuthorize("isAnonymous()")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@ModelAttribute("regDto") @Valid RegisterUserDto regDto,
                          Model model) {
        UUID uuid = userService.saveUser(regDto);
    }
}
