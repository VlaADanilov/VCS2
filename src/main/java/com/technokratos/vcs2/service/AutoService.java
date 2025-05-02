package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
public interface AutoService {

    UUID addAuto(@Valid AutoRequestDto auto);

    List<ListElementAutoResponseDto> getAllAutos(int page, int size, UUID brand, String sort, String order);

    List<AutoResponseDto> getAutosByName(String name);

    AutoResponseDto getAutoById(UUID id);

    void updateAuto(@Valid AutoRequestDto auto, UUID id);

    void deleteAuto(UUID id);

    boolean isOwner(UUID autoId, String username);

    List<ListElementAutoResponseDto> getAllAutoFromUser(UUID userId,int page, int size, String sort, String order, UUID brand_id);

    void checkForExistsAuto(UUID id);

    void addImageToAuto(UUID autoId, UUID imageId);

    Long getAllAutosPagesCount();

    Long getAllAutosPagesCount(UUID brandId);

    Long getAutoPagesCount(UUID userId);
}
