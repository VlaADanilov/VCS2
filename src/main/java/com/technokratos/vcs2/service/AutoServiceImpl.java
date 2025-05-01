package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.ImageNotFoundException;
import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.mapper.AutoMapper;
import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Image;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.ImageRepository;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {
    private final AutoRepository autoRepository;
    private final BrandService brandService;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final AutoMapper autoMapper;
    @Override
    public UUID addAuto(AutoRequestDto auto) {
        UUID autoId = UUID.randomUUID();
        Auto result = autoMapper.toAuto(auto);
        result.setId(autoId);
        User user = UserReturner.getCurrentUser().get();
        result.setUser(user);
        autoRepository.save(result);
        return autoId;
    }

    @Override
    public List<ListElementAutoResponseDto> getAllAutos(int page, int size) {
        List<Auto> all = autoRepository.findAll(PageRequest.of(page, size)).get().toList();
        return getListElementAutoResponseDtos(all);
    }

    @Override
    public List<AutoResponseDto> getAutosByName(String name) {
        return List.of();
    }

    @Override
    public AutoResponseDto getAutoById(UUID id) {
        Optional<Auto> byId = autoRepository.findById(id);
        if (byId.isPresent()) {
            Auto auto = byId.get();
            return autoMapper.toAutoResponseDto(auto);
        } else {
            throw new AutoNotFoundException(id);
        }
    }

    @Override
    public void updateAuto(AutoRequestDto auto, UUID id) {
        checkForExistsAuto(id);
        Auto result = autoMapper.toAuto(auto);
        result.setId(id);
        result.setUser(UserReturner.getCurrentUser().get());
        Auto auto1 = autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(id));
        result.setImages(auto1.getImages());
        autoRepository.save(result);
    }

    public void checkForExistsAuto(UUID id) {
        if (!autoRepository.existsById(id)) {
            throw new AutoNotFoundException(id);
        }
    }

    @Override
    public void addImageToAuto(UUID autoId, UUID imageId) {
        Auto auto = autoRepository.findById(autoId).orElseThrow(() -> new AutoNotFoundException(autoId));
        auto.getImages().add(imageRepository.getReferenceById(imageId));
        autoRepository.save(auto);
    }

    @Override
    public void deleteAuto(UUID id) {
        Auto auto = autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(id));
        List<Image> list = new ArrayList<>();
        for (Image image : auto.getImages()) {
            list.add(image);
        }

        for (Image image : list) {
            imageService.deleteImage(image.getId(), id);
        }

        autoRepository.deleteById(id);
    }

    @Override
    public List<ListElementAutoResponseDto> getAllAutoFromUser(UUID userId,int page, int size) {
        Page<Auto> list = autoRepository.getPageableAutoFromUser(userId, PageRequest.of(page,size));
        List<Auto> all = list.toList();
        return getListElementAutoResponseDtos(all);
    }

    private List<ListElementAutoResponseDto> getListElementAutoResponseDtos(List<Auto> all) {
        List<ListElementAutoResponseDto> rez = all.stream().map(autoMapper::toListElementAutoResponseDto).toList();
        return rez;
    }

    public boolean isOwner(UUID autoId, String username) {
        return autoRepository.findById(autoId)
                .map(auto -> auto.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
