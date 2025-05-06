package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.UserApi;
import com.technokratos.vcs2.model.dto.response.UserResponseDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApi {
    private final UserServiceImpl userService;
    private final AutoService autoService;
    private final BrandService brandService;


    public String getAllUsers(int page,
                              int size,
                              String search,
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


    public String getAllAutosFrom(UUID user_id,
                                  Model model,
                                  int page,
                                  int size,
                                  String sort,
                                  String order,
                                  UUID brand_id ) {
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
