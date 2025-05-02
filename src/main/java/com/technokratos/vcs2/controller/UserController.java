package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.response.UserResponseDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.UserRepository;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final AutoService autoService;

    @GetMapping
    public String getAllUsers(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        model.addAttribute("list", userService.getAllUsers(page - 1, size));
        model.addAttribute("can", canChangeRole());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", userService.countOfAllUserPages());
        return "list_of_users";
    }

    @GetMapping("/{user_id}/auto")
    public String getAllAutosFrom(@PathVariable("user_id") UUID user_id,
                                  Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("list", autoService.getAllAutoFromUser(user_id, page - 1, size));
        model.addAttribute("myPath", "/user/" + user_id + "/auto");
        model.addAttribute("back", "/user");
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", autoService.getAutoPagesCount(user_id));
        return "list_of_auto";
    }

    private boolean canChangeRole() {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        if (currentUser.isEmpty()) {
            return false;
        }
        return currentUser.get().getRole().equals("ROLE_ADMIN");
    }

}
