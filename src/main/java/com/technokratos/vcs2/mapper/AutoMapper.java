package com.technokratos.vcs2.mapper;

import com.technokratos.vcs2.model.dto.request.AutoRequestDto;
import com.technokratos.vcs2.model.dto.response.AutoResponseDto;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoMapper {
    private final BrandService brandService;
    public ListElementAutoResponseDto toListElementAutoResponseDto(Auto auto) {
        return new ListElementAutoResponseDto(
                auto.getId(),
                auto.getUser().getUsername(),
                new AutoResponseDto(
                        auto.getModel(),
                        auto.getYear(),
                        auto.getPrice(),
                        auto.getMileage(),
                        auto.getCity(),
                        auto.getDescription(),
                        auto.getPhone(),
                        auto.getBrand().getId(),
                        auto.getImages().stream().map((a) -> a.getId()).toList()
                ),
                auto.getBrand().getName()
        );
    }

    public AutoResponseDto toAutoResponseDto(Auto auto) {
        return new AutoResponseDto(
                auto.getModel(),
                auto.getYear(),
                auto.getPrice(),
                auto.getMileage(),
                auto.getCity(),
                auto.getDescription(),
                auto.getPhone(),
                auto.getBrand().getId(),
                auto.getImages().stream().map((a) -> a.getId()).toList()
        );
    }

    public Auto toAuto(AutoRequestDto auto) {
        return Auto.builder()
                .model(auto.getModel())
                .year(auto.getYear())
                .price(auto.getPrice())
                .mileage(auto.getMileage())
                .city(auto.getCity())
                .description(auto.getDescription())
                .brand(brandService.getReferenceById(auto.getBrand_id()))
                .phone(auto.getPhone())
                .build();
    };
}
