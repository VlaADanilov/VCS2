package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.AutoApi;
import com.technokratos.vcs2.model.Role;
import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.BrandResponseDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.LikeService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AutoController implements AutoApi {
    private final AutoService autoService;
    private final BrandService brandService;
    private final UserServiceImpl userService;
    private final LikeService likeService;

    public String getAllAutoPageable(
            int page,
            int size,
            Model model,
            String sort,
            String order,
            UUID brand_id) {
        model.addAttribute("list",autoService.getAllAutos(page - 1, size,brand_id, sort, order));
        model.addAttribute("myPath","/auto");
        model.addAttribute("back","/");
        if(brand_id == null) {
            Long allAutosPagesCount = autoService.getAllAutosPagesCount();
            model.addAttribute("pageCount", allAutosPagesCount == 0? 1L : allAutosPagesCount);
        } else {
            model.addAttribute("pageCount", autoService.getAllAutosPagesCount(brand_id));
        }
        List<BrandResponseDto> brands = brandService.getBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("currentPage", page);
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("brand", brand_id);
        return "list_of_auto";
    }


    public String getAuto(UUID carId,
                          Model model,
                          String referer) {
        model.addAttribute("backUrl",correctReferer(referer));
        model.addAttribute("auto", autoService.getAutoById(carId));
        model.addAttribute("brand", brandService.getBrandByAutoId(carId));
        model.addAttribute("user", userService.findUserByCarId(carId));
        model.addAttribute("showIcons",showIcons(carId));
        model.addAttribute("autoId", carId.toString());
        boolean isAuthorize = false;
        if (UserReturner.getCurrentUser().isPresent()) {
            isAuthorize = true;
            User user = UserReturner.getCurrentUser().get();
            model.addAttribute("hasLike", likeService.existLike(user.getId(), carId));
        }
        model.addAttribute("isAuthorize", isAuthorize);
        return "auto";
    }

    private static final List<String> correctReferers = List.of(
            "/auto",
            "/auto/myCars",
            "/like",
            "/report"
    );

    private String correctReferer(String referer) {
        if (correctReferers.contains(referer)) {
            return referer;
        }
        if (referer.startsWith("/user/")) {
            return referer;
        }
        return "/auto";
    }


    public String addAutoForm(Model model) {
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("auto", new AutoResponseDto("",
                LocalDate.now().getYear(),
                0,
                0,
                "",
                "",
                "",
                UUID.randomUUID(),
                List.of()));
        model.addAttribute("method", "POST");
        model.addAttribute("back", "/");
        return "update_auto_form";
    }


    public UUID addAuto(AutoRequestDto auto) {
        log.info("New auto add request: {}", auto.toString());
        return autoService.addAuto(auto);
    }


    public void deleteAuto(UUID carId) {
        log.info("Delete auto request: {}", carId);
        autoService.deleteAuto(carId);
    }

    public String updateAutoForm(UUID carId, Model model) {
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("auto", autoService.getAutoById(carId));
        model.addAttribute("autoId", carId.toString());
        model.addAttribute("method", "PUT");
        model.addAttribute("back", "/auto/%s".formatted(carId));
        return "update_auto_form";
    }


    public void updateAuto(UUID carId, AutoRequestDto auto) {
        log.info("Update auto wtih id {} request: {}", carId,auto.toString());
        autoService.updateAuto(auto, carId);
    }


    public String getMyAutoList(int page,
                                int size,
                                String sort,
                                String order,
                                UUID brand_id,
                                Model model) {
        model.addAttribute("list",autoService.getAllAutoFromUser(
                UserReturner.getCurrentUser().get().getId(),
                page - 1,
                size,
                sort,order,brand_id));
        model.addAttribute("myPath","/auto/myCars");
        model.addAttribute("back", "/");
        UUID id = UserReturner.getCurrentUser().get().getId();
        Long autoPagesCount = autoService.getAutoPagesCount(id, brand_id);
        model.addAttribute("pageCount", autoPagesCount == 0?1:autoPagesCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("brand", brand_id);
        return "list_of_auto";
    }

    private boolean showIcons(UUID autoId) {
        Optional<User> currentUser = UserReturner.getCurrentUser();
        if(currentUser.isEmpty()) {
            return false;
        }
        User user = currentUser.get();
        return autoService.isOwner(autoId, user.getUsername()) || user.getRole().equals(Role.ROLE_ADMIN.toString()) || user.getRole().equals(Role.ROLE_MODERATOR.toString());
    }
}
