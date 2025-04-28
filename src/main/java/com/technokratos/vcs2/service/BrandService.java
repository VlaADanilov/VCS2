package com.technokratos.vcs2.service;

import com.technokratos.vcs2.model.entity.Brand;

import java.util.List;
import java.util.UUID;

public interface BrandService {
    Brand getBrandByAutoId(UUID autoId);

    List<Brand> getBrands();

    Brand getReferenceById(UUID brandId);
}
