package com.technokratos.vcs2.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping
    String getAllUsers(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String search,
                       Model model);

    @GetMapping("/{user_id}/auto")
    String getAllAutosFrom(@PathVariable("user_id") UUID user_id,
                           Model model,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String sort,
                           @RequestParam(required = false) String order,
                           @RequestParam(required = false) UUID brand_id );
}
