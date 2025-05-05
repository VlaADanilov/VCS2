package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.GeneralApi;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.util.UserReturner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
public class GeneralMenuController implements GeneralApi {

    public String index(Model model) {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        currentUser.ifPresent(user -> model.addAttribute("user", user));
        return "general_menu";
    }
}
