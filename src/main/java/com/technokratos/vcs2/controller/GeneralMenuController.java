package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GeneralMenuController {

    @GetMapping()
    public String index(Model model) {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        currentUser.ifPresent(user -> model.addAttribute("user", user));
        return "general_menu";
    }
}
