package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.service.AutoService;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "list_of_auto";
    }

    @GetMapping("/{car_id}")
    public String getAuto(@PathVariable("car_id") UUID carId, Model model) {
        model.addAttribute("auto", autoService.getAutoById(carId));
        model.addAttribute("brand", brandService.getBrandByAutoId(carId));
        model.addAttribute("user", userService.findUserByCarId(carId));
        //TODO images
        return "auto";
    }
}
