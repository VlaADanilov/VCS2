package com.technokratos.vcs2.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping()
public interface GeneralApi {
    @GetMapping("/")
    String index(Model model);
}
