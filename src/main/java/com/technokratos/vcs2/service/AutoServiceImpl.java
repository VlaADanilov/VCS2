package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.User;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutoServiceImpl implements AutoService {
    private final AutoRepository autoRepository;
    private final BrandService brandService;
    @Override
    public UUID addAuto(AutoRequestDto auto) {
        UUID autoId = UUID.randomUUID();
        Auto result = Auto.builder()
                .id(autoId)
                .model(auto.getModel())
                .city(auto.getCity())
                .year(auto.getYear())
                .price(auto.getPrice())
                .mileage(auto.getMileage())
                .description(auto.getDescription())
                .brand(brandService.getReferenceById(auto.getBrand_id()))
                .build();
        User user = UserReturner.getCurrentUser().get();
        result.setUser(user);
        autoRepository.save(result);
        return autoId;
    }

    @Override
    public List<ListElementAutoResponseDto> getAllAutos(int page, int size) {
        List<Auto> all = autoRepository.findAll(PageRequest.of(page, size)).get().toList();
        List<ListElementAutoResponseDto> list = all.stream().map(auto -> {
            ListElementAutoResponseDto elem = new ListElementAutoResponseDto(
                    auto.getId(),
                    auto.getUser().getUsername(),
                    new AutoResponseDto(
                            auto.getModel(),
                            auto.getYear(),
                            auto.getPrice(),
                            auto.getMileage(),
                            auto.getCity(),
                            auto.getDescription(),
                            auto.getPhone()
                    ),
                    auto.getBrand().getName()
            );
            return elem;
        }).toList();
        return list;
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
            return new AutoResponseDto(
                    auto.getModel(),
                    auto.getYear(),
                    auto.getPrice(),
                    auto.getMileage(),
                    auto.getCity(),
                    auto.getDescription(),
                    auto.getPhone()
            );
        } else {
            throw new AutoNotFoundException(id);
        }
    }

    @Override
    public void updateAuto(AutoRequestDto auto, UUID id) {

    }

    @Override
    public void deleteAuto(UUID id) {
        if (!autoRepository.existsById(id)) {
            throw new AutoNotFoundException(id);
        }
        autoRepository.deleteById(id);
    }

    public boolean isOwner(UUID autoId, String username) {
        return autoRepository.findById(autoId)
                .map(auto -> auto.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
