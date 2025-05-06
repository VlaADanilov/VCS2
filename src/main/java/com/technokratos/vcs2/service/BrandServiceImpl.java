package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.BrandNotFoundException;
import com.technokratos.vcs2.model.dto.response.BrandResponseDto;
import com.technokratos.vcs2.model.entity.Brand;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final AutoRepository autoRepository;

    @Override
    public BrandResponseDto getBrandByAutoId(UUID autoId) {
        log.info("Getting brand by auto ID: {}", autoId);

        if (!autoRepository.existsById(autoId)) {
            log.warn("Auto with ID {} not found", autoId);
            throw new AutoNotFoundException(autoId);
        }

        Brand brandFromCarByCarId = brandRepository.getBrandFromCarByCarId(autoId);
        log.info("Found brand for auto {}: {}", autoId, brandFromCarByCarId.getName());

        return new BrandResponseDto(
                brandFromCarByCarId.getId(),
                brandFromCarByCarId.getName(),
                brandFromCarByCarId.getCountry()
        );
    }

    @Override
    public List<BrandResponseDto> getBrands() {
        log.info("Fetching all brands");
        List<Brand> brands = brandRepository.findAll();

        List<BrandResponseDto> responseDtos = brands.stream()
                .map(b -> new BrandResponseDto(b.getId(), b.getName(), b.getCountry()))
                .toList();

        log.info("Fetched {} brands", responseDtos.size());
        return responseDtos;
    }

    @Override
    public Brand getReferenceById(UUID brandId) {
        log.debug("Getting reference to brand with ID: {}", brandId);

        boolean exists = brandRepository.existsById(brandId);
        if (!exists) {
            log.warn("Brand with ID {} not found", brandId);
            throw new BrandNotFoundException(brandId);
        }

        Brand brand = brandRepository.getReferenceById(brandId);
        log.debug("Reference to brand {} retrieved successfully", brandId);
        return brand;
    }
}
