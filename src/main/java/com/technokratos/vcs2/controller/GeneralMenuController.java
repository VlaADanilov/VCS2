package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.repository.AutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class GeneralMenuController {

    @GetMapping()
    public String index(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "general_menu";
    }
}
