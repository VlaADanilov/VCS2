package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.entity.Brand;
import com.technokratos.vcs2.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    @Override
    public Brand getBrandByAutoId(UUID autoId) {
        return brandRepository.getBrandFromCarByCarId(autoId);
    }
}
