package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.BrandNotFoundException;
import com.technokratos.vcs2.model.dto.response.BrandResponseDto;
import com.technokratos.vcs2.model.entity.Brand;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final AutoRepository autoRepository;
    @Override
    public BrandResponseDto getBrandByAutoId(UUID autoId) {
        if (!autoRepository.existsById(autoId)) {
            throw new AutoNotFoundException(autoId);
        }
        Brand brandFromCarByCarId = brandRepository.getBrandFromCarByCarId(autoId);
        return new BrandResponseDto(brandFromCarByCarId.getId(),
                                    brandFromCarByCarId.getName(),
                                    brandFromCarByCarId.getCountry());
    }

    @Override
    public List<BrandResponseDto> getBrands() {
        return brandRepository.findAll().stream().map(
                (a) -> new BrandResponseDto(a.getId(),a.getName(),a.getCountry())
        ).toList();
    }

    @Override
    public Brand getReferenceById(UUID brandId) {
        boolean exists = brandRepository.existsById(brandId);
        if (!exists) {
            throw new BrandNotFoundException(brandId);
        }
        return brandRepository.getReferenceById(brandId);
    }
}
