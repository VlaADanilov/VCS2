package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.GeneralApi;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.util.UserReturner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;

@Controller
@Slf4j
public class GeneralMenuController implements GeneralApi {

    public String index(Model model) {
        log.info("Request for general menu");
        Optional<User> currentUser = UserReturner.getCurrentUser();
        currentUser.ifPresent(user -> model.addAttribute("user", user));
        log.info("User is {}", currentUser);
        return "general_menu";
    }
}
