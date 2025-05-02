package com.technokratos.vcs2.controller;

import com.technokratos.vcs2.service.LikeService;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    @GetMapping
    public String like(Model model,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size) {
        model.addAttribute("list",likeService.getAutoWhoUserLike(UserReturner
                    .getCurrentUser()
                    .get()
                    .getId(),
                page - 1,
                size));
        model.addAttribute("myPath","/like");
        model.addAttribute("back","/");
        Long allLikesCountPages = likeService.getAllLikesCountPages(UserReturner.getCurrentUser().get().getId());
        if (allLikesCountPages == 0) allLikesCountPages = 1L;
        model.addAttribute("pageCount", allLikesCountPages);
        model.addAttribute("currentPage", page);
        return "list_of_auto";
    }

    @PostMapping("/{auto_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable("auto_id") UUID auto_id) {
        likeService.addLike(UserReturner.getCurrentUser().get().getId(), auto_id);
    }

    @DeleteMapping("/{auto_id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable("auto_id") UUID auto_id) {
        likeService.removeLike(UserReturner.getCurrentUser().get().getId(), auto_id);
    }
}
