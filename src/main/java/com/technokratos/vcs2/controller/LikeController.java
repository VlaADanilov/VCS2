package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.api.LikeApi;
import com.technokratos.vcs2.service.BrandService;
import com.technokratos.vcs2.service.LikeService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LikeController implements LikeApi {
    private final LikeService likeService;
    private final BrandService brandService;

    public String like(Model model,
                       int page,
                       int size,
                       String sort,
                       String order,
                       UUID brand_id) {
        model.addAttribute("list",likeService.getAutoWhoUserLike(UserReturner
                    .getCurrentUser()
                    .get()
                    .getId(),
                page - 1,
                size,
                sort,order,brand_id));
        model.addAttribute("myPath","/like");
        model.addAttribute("back","/");
        Long allLikesCountPages = likeService.getAllLikesCountPages(UserReturner.getCurrentUser().get().getId(), brand_id);
        if (allLikesCountPages == 0) allLikesCountPages = 1L;
        model.addAttribute("pageCount", allLikesCountPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("brands", brandService.getBrands());
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("brand", brand_id);
        return "list_of_auto";
    }


    public void addLike(UUID auto_id) {
        log.info("Add like request to auto_id: {}, from user with id {}", auto_id, UserReturner.getCurrentUser().get().getId());
        likeService.addLike(UserReturner.getCurrentUser().get().getId(), auto_id);
    }


    public void deleteLike(UUID auto_id) {
        log.info("Delete like from auto with id {}, from user with id {}", auto_id, UserReturner.getCurrentUser().get().getId());
        likeService.removeLike(UserReturner.getCurrentUser().get().getId(), auto_id);
    }
}
