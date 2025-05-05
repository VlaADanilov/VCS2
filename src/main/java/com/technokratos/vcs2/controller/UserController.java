package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.dto.response.UserResponseDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final AutoService autoService;
    private final BrandService brandService;

    @GetMapping
    public String getAllUsers(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String search,
                              Model model) {
        List<UserResponseDto> list;
        Long countOfPage;
        if (search != null) {
            list = userService.searchUsers(search,page - 1,size);
            countOfPage = userService.countOfAllUserPages(search);
        } else {
            list = userService.getAllUsers(page - 1, size);
            countOfPage = userService.countOfAllUserPages();
        }
        model.addAttribute("list", list);
        model.addAttribute("can", canChangeRole());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageCount", countOfPage);
        model.addAttribute("search", search);
        return "list_of_users";
    }

    @GetMapping("/{user_id}/auto")
    public String getAllAutosFrom(@PathVariable("user_id") UUID user_id,
                                  Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String sort,
                                  @RequestParam(required = false) String order,
                                  @RequestParam(required = false) UUID brand_id ) {
        model.addAttribute("list", autoService.getAllAutoFromUser(user_id, page - 1, size,sort,order,brand_id));
        model.addAttribute("myPath", "/user/" + user_id + "/auto");
        model.addAttribute("back", "/user");
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("pageCount", autoService.getAutoPagesCount(user_id, brand_id));
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("brand", brand_id);
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
