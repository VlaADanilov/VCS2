package com.technokratos.vcs2.controller;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller()
@RequestMapping("/auto")
@Tag(name = "AutoController", description = "Возможности, связанные с авто")
@RequiredArgsConstructor
public class AutoController {
    private final AutoService autoService;
    private final BrandService brandService;
    private final UserServiceImpl userService;
    private final LikeService likeService;

    @GetMapping
    public String getAllAutoPageable(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) UUID brand_id) {
        model.addAttribute("list",autoService.getAllAutos(page - 1, size,brand_id, sort, order));
        model.addAttribute("myPath","/auto");
        model.addAttribute("back","/");
        if(brand_id == null) {
            model.addAttribute("pageCount", autoService.getAllAutosPagesCount());
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

    @GetMapping("/{car_id}")
    public String getAuto(@PathVariable("car_id") UUID carId,
                          Model model,
                          @RequestParam(required = false, defaultValue = "/auto") String referer) {
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

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
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

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Operation(
            summary = "Добавление объявления",
            description = "Позволяет добавить объявление по продаже авто"
    )
    public UUID addAuto(@RequestBody AutoRequestDto auto) {
        return autoService.addAuto(auto);
    }

    @DeleteMapping("/{car_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    public void deleteAuto(@PathVariable("car_id") UUID carId) {
        autoService.deleteAuto(carId);
    }

    @GetMapping("/{car_id}/updateForm")
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    public String updateAutoForm(@PathVariable("car_id") UUID carId, Model model) {
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("auto", autoService.getAutoById(carId));
        model.addAttribute("autoId", carId.toString());
        model.addAttribute("method", "PUT");
        model.addAttribute("back", "/auto/%s".formatted(carId));
        return "update_auto_form";
    }

    @PutMapping("/{car_id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PreAuthorize("@securityService.canDelete(#carId, authentication.name) " +
            "or hasAnyRole('ADMIN', 'MODERATOR')")
    public void updateAuto(@PathVariable("car_id") UUID carId, @RequestBody AutoRequestDto auto) {
        autoService.updateAuto(auto, carId);
    }

    @GetMapping("/myCars")
    @PreAuthorize("isAuthenticated()")
    public String getMyAutoList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) String sort,
                                @RequestParam(required = false) String order,
                                @RequestParam(required = false) UUID brand_id,
                                Model model) {
        model.addAttribute("list",autoService.getAllAutoFromUser(
                UserReturner.getCurrentUser().get().getId(),
                page - 1,
                size,
                sort,order,brand_id));
        model.addAttribute("myPath","/auto/myCars");
        model.addAttribute("back", "/");
        model.addAttribute("pageCount", autoService.getAutoPagesCount(UserReturner.getCurrentUser().get().getId(), brand_id));
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
