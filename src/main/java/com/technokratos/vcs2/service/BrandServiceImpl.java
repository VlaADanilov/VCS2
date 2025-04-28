package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.notFound.BrandNotFoundException;
import com.technokratos.vcs2.model.entity.Brand;
import com.technokratos.vcs2.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public Brand getBrandByAutoId(UUID autoId) {
        return brandRepository.getBrandFromCarByCarId(autoId);
    }

    @Override
    public List<Brand> getBrands() {
        return brandRepository.findAll();
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
