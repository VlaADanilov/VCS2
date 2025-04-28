package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.dto.response.BrandResponseDto;
import com.technokratos.vcs2.model.entity.Brand;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    BrandResponseDto getBrandByAutoId(UUID autoId);

    List<BrandResponseDto> getBrands();

    Brand getReferenceById(UUID brandId);
}
