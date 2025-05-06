package com.technokratos.vcs2.service;

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
import com.technokratos.vcs2.repository.UserRepository;
import com.technokratos.vcs2.util.UserReturner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AutoServiceImpl implements AutoService {
    private final AutoRepository autoRepository;
    private final BrandService brandService;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final AutoMapper autoMapper;
    private final EntityManager entityManager;
    private final UserServiceImpl userService;

    @Override
    public UUID addAuto(AutoRequestDto auto) {
        log.info("Adding new auto with data: {}", auto);
        UUID autoId = UUID.randomUUID();
        Auto result = autoMapper.toAuto(auto);
        result.setId(autoId);
        User user = UserReturner.getCurrentUser().get();
        result.setUser(user);
        autoRepository.save(result);
        log.info("Auto added successfully with ID: {}", autoId);
        return autoId;
    }

    @Override
    public List<ListElementAutoResponseDto> getAllAutos(int page, int size, UUID brand, String sort, String order) {
        log.info("Fetching all autos (page: {}, size: {}, brand: {}, sort: {}, order: {})", page, size, brand, sort, order);
        List<Auto> all = findFilteredAndSorted(brand, sort, order, PageRequest.of(page, size), null);
        List<ListElementAutoResponseDto> responseDtos = getListElementAutoResponseDtos(all);
        log.info("Found {} autos", responseDtos.size());
        return responseDtos;
    }

    @Override
    public List<AutoResponseDto> getAutosByName(String name) {
        log.warn("Method getAutosByName is not implemented yet");
        return List.of();
    }

    @Override
    public AutoResponseDto getAutoById(UUID id) {
        log.info("Fetching auto by ID: {}", id);
        Optional<Auto> byId = autoRepository.findById(id);
        if (byId.isPresent()) {
            Auto auto = byId.get();
            log.info("Auto found: {}", auto.getId());
            return autoMapper.toAutoResponseDto(auto);
        } else {
            log.warn("Auto not found with ID: {}", id);
            throw new AutoNotFoundException(id);
        }
    }

    @Override
    public void updateAuto(AutoRequestDto auto, UUID id) {
        log.info("Updating auto with ID: {}", id);
        checkForExistsAuto(id);
        Auto result = autoMapper.toAuto(auto);
        result.setId(id);
        result.setUser(UserReturner.getCurrentUser().get());
        Auto auto1 = autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(id));
        result.setImages(auto1.getImages());
        autoRepository.save(result);
        log.info("Auto updated successfully: {}", id);
    }

    public void checkForExistsAuto(UUID id) {
        if (!autoRepository.existsById(id)) {
            log.warn("Auto does not exist with ID: {}", id);
            throw new AutoNotFoundException(id);
        }
    }

    @Override
    public void addImageToAuto(UUID autoId, UUID imageId) {
        log.info("Adding image {} to auto {}", imageId, autoId);
        Auto auto = autoRepository.findById(autoId).orElseThrow(() -> new AutoNotFoundException(autoId));
        auto.getImages().add(imageRepository.getReferenceById(imageId));
        autoRepository.save(auto);
        log.info("Image added successfully to auto {}", autoId);
    }

    @Override
    public Long getAllAutosPagesCount() {
        long count = autoRepository.count();
        Long totalPages = count % 10 == 0 ? count / 10 : count / 10 + 1;
        log.info("Total pages for all autos: {}", totalPages);
        return totalPages;
    }

    @Override
    public void deleteAuto(UUID id) {
        log.info("Deleting auto with ID: {}", id);
        Auto auto = autoRepository.findById(id).orElseThrow(() -> new AutoNotFoundException(id));
        List<Image> list = new ArrayList<>(auto.getImages());

        for (Image image : list) {
            log.info("Deleting associated image: {}", image.getId());
            imageService.deleteImage(image.getId(), id);
        }

        autoRepository.deleteById(id);
        log.info("Auto deleted successfully: {}", id);
    }

    @Override
    public List<ListElementAutoResponseDto> getAllAutoFromUser(UUID userId, int page, int size, String sort, String order, UUID brand_id) {
        log.info("Fetching autos for user ID: {}", userId);
        userService.checkForExist(userId);
        List<Auto> list = findFilteredAndSorted(brand_id, sort, order, PageRequest.of(page,size), userId);
        List<ListElementAutoResponseDto> responseDtos = getListElementAutoResponseDtos(list);
        log.info("Found {} autos for user {}", responseDtos.size(), userId);
        return responseDtos;
    }

    private List<ListElementAutoResponseDto> getListElementAutoResponseDtos(List<Auto> all) {
        log.debug("Mapping {} autos to ListElementAutoResponseDto", all.size());
        List<ListElementAutoResponseDto> rez = all.stream().map(autoMapper::toListElementAutoResponseDto).toList();
        log.debug("Mapped {} autos to Dto", rez.size());
        return rez;
    }

    public boolean isOwner(UUID autoId, String username) {
        log.debug("Checking if user {} owns auto {}", username, autoId);
        return autoRepository.findById(autoId)
                .map(auto -> {
                    boolean isOwner = auto.getUser().getUsername().equals(username);
                    log.debug("Ownership check result for {}: {}", autoId, isOwner);
                    return isOwner;
                })
                .orElse(false);
    }

    @Override
    public Long getAutoPagesCount(UUID userID, UUID brandID) {
        log.info("Calculating pages for user {} and brand {}", userID, brandID);
        Long l;
        if (brandID == null) {
            l = autoRepository.countOf(userID);
        } else {
            l = autoRepository.countOf(userID, brandID);
        }
        Long totalPages = l % 10 == 0 ? l / 10 : l / 10 + 1;
        log.info("Total pages for user {} and brand {}: {}", userID, brandID, totalPages);
        return totalPages;
    }

    public List<Auto> findFilteredAndSorted(UUID brand, String sort, String order, Pageable pageable, UUID user) {
        log.info("Filtering and sorting autos (brand: {}, sort: {}, order: {}, user: {})", brand, sort, order, user);
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
        List<Auto> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        log.info("Filtered and sorted autos: {} items", result.size());
        return result;
    }

    @Override
    public Long getAllAutosPagesCount(UUID brandId) {
        log.info("Calculating total pages for brand {}", brandId);
        Long l = autoRepository.countOfBrandCars(brandId);
        if (l == 0) l = 1L;
        Long totalPages = l % 10 == 0 ? l / 10 : l / 10 + 1;
        log.info("Total pages for brand {}: {}", brandId, totalPages);
        return totalPages;
    }
}