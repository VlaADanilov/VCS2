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

    List<ListElementAutoResponseDto> getAllAutos(int page, int size);

    List<AutoResponseDto> getAutosByName(String name);

    AutoResponseDto getAutoById(UUID id);

    void updateAuto(AutoRequestDto auto, UUID id);

    void deleteAuto(UUID id);


}
