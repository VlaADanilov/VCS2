package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;

import java.util.List;
import java.util.UUID;

public interface LikeService {
    List<ListElementAutoResponseDto> getAutoWhoUserLike(UUID userId, int page, int size, String sort, String order, UUID brand_id);

    void addLike(UUID userId, UUID autoId);

    void removeLike(UUID userId, UUID autoId);

    boolean existLike(UUID userId, UUID autoId);

    Long getAllLikesCountPages(UUID id, UUID brandId);
}
