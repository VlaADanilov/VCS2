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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
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
    private final EntityManager entityManager;
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
    public List<ListElementAutoResponseDto> getAllAutos(int page, int size, UUID brand, String sort, String order) {
        List<Auto> all = findFilteredAndSorted(brand,sort,order, PageRequest.of(page, size), null);
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
    public Long getAllAutosPagesCount() {
        long count = autoRepository.count();
        return count % 10 == 0 ? count / 10 : count / 10 + 1;
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
    public List<ListElementAutoResponseDto> getAllAutoFromUser(UUID userId,int page, int size, String sort, String order, UUID brand_id) {
        List<Auto> list = findFilteredAndSorted(brand_id, sort, order, PageRequest.of(page,size), userId);
        return getListElementAutoResponseDtos(list);
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

    @Override
    public Long getAutoPagesCount(UUID userID, UUID brandID) {
        Long l;
        if (brandID == null) {
            l = autoRepository.countOf(userID);
        } else {
            l = autoRepository.countOf(userID, brandID);
        }
        return l % 10 == 0 ? l / 10 : l / 10 + 1;
    }

    public List<Auto> findFilteredAndSorted(UUID brand, String sort, String order, Pageable pageable, UUID user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Auto> query = cb.createQuery(Auto.class);
        Root<Auto> root = query.from(Auto.class);

        // Фильтр по бренду
        List<Predicate> predicates = new ArrayList<>();
        if (brand != null) {
            predicates.add(cb.equal(root.get("brand").get("id"), brand));
        }
        if (user != null) {
            predicates.add(cb.equal(root.get("user").get("id"), user));
        }
        // Сортировка
        Order orderBy = null;
        if (sort != null) {
            switch (sort) {
                case "price":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("price")) : cb.asc(root.get("price"));
                    break;
                case "mileage":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("mileage")) : cb.asc(root.get("mileage"));
                    break;
                case "year":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("year")) : cb.asc(root.get("year"));
                    break;
                default:
                    orderBy = cb.asc(root.get("id")); // По умолчанию
            }
        } else {
            orderBy = cb.desc(root.get("id"));
        }
        query.orderBy(orderBy);

        // Сборка и выполнение запроса
        query.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public Long getAllAutosPagesCount(UUID brandId) {
        Long l = autoRepository.countOfBrandCars(brandId);
        if (l == 0) l = 1L;
        return l % 10 == 0? l / 10 : l / 10 + 1;
    }
}
