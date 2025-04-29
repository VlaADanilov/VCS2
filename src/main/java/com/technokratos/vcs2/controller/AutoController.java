package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.model.Role;
import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.UserServiceImpl;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller()
@RequestMapping("/auto")
@RequiredArgsConstructor
public class AutoController {
    private final AutoService autoService;
    private final BrandService brandService;
    private final UserServiceImpl userService;

    @GetMapping
    public String getAllAutoPageable(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        model.addAttribute("list",autoService.getAllAutos(page, size));
        model.addAttribute("myPath","/auto");
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
        //TODO images
        return "auto";
    }

    private static final List<String> correctReferers = List.of(
            "/auto",
            "/auto/myCars"
    );

    private String correctReferer(String referer) {
        if (correctReferers.contains(referer)) {
            return referer;
        }
        return "/auto";
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addAutoForm(Model model) {
        model.addAttribute("brands", brandService.getBrands());

        return "add_auto_form";
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
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
        return "update_auto_form.html";
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
    public String getMyAutoList(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        model.addAttribute("list",autoService.getAllAutoFromUser(
                UserReturner.getCurrentUser().get().getId(),
                page,
                size));
        model.addAttribute("myPath","/auto/myCars");
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
