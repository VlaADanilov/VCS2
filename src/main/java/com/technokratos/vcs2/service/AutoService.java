package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;

import java.util.List;
import java.util.UUID;

public interface AutoService {

    UUID addAuto(AutoRequestDto auto);

    List<ListElementAutoResponseDto> getAllAutos(int page, int size);

    List<AutoResponseDto> getAutosByName(String name);

    AutoResponseDto getAutoById(UUID id);

    void updateAuto(AutoRequestDto auto, UUID id);

    void deleteAuto(UUID id);


}
