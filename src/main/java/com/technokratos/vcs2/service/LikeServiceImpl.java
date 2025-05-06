package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.LikeException;
import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.mapper.AutoMapper;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import com.technokratos.vcs2.model.entity.Like;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.LikeRepository;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AutoRepository autoRepository;
    private final AutoMapper autoMapper;
    private final EntityManager entityManager;

    @Override
    public List<ListElementAutoResponseDto> getAutoWhoUserLike(UUID userId, int page, int size, String sort, String order, UUID brand_id) {
        log.info("Fetching liked autos for user ID: {}", userId);

        List<Like> allByUser = findFilteredAndSorted(brand_id, sort, order, PageRequest.of(page, size), userId);
        List<ListElementAutoResponseDto> responseDtos = allByUser.stream()
                .map(a -> autoMapper.toListElementAutoResponseDto(a.getAuto()))
                .toList();

        log.info("Found {} liked autos for user {}", responseDtos.size(), userId);
        return responseDtos;
    }

    @Override
    public void addLike(UUID userId, UUID autoId) {
        log.info("Adding like from user {} to auto {}", userId, autoId);

        checkUserAndAuto(userId, autoId);

        if (likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId))) {
            log.warn("User {} already liked auto {}", userId, autoId);
            throw new LikeException("like already exists");
        }

        Like like = new Like();
        like.setUser(userRepository.getReferenceById(userId));
        like.setAuto(autoRepository.getReferenceById(autoId));
        like.setId(UUID.randomUUID());
        likeRepository.save(like);

        log.info("Like added successfully from user {} to auto {}", userId, autoId);
    }

    private void checkUserAndAuto(UUID userId, UUID autoId) {
        log.debug("Checking existence of user {} and auto {}", userId, autoId);

        if (!userRepository.existsById(userId)) {
            log.warn("User with ID {} not found", userId);
            throw new UserNotFoundException(userId);
        }
        if (!autoRepository.existsById(autoId)) {
            log.warn("Auto with ID {} not found", autoId);
            throw new AutoNotFoundException(autoId);
        }
    }

    @Override
    @Transactional
    public void removeLike(UUID userId, UUID autoId) {
        log.info("Removing like from user {} to auto {}", userId, autoId);

        checkUserAndAuto(userId, autoId);

        if (!likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId))) {
            log.warn("User {} did not like auto {}", userId, autoId);
            throw new LikeException("like not exists");
        }

        likeRepository.deleteByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId));

        log.info("Like removed successfully from user {} to auto {}", userId, autoId);
    }

    @Override
    public boolean existLike(UUID userId, UUID autoId) {
        boolean result = likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId)
        );

        log.debug("Check if user {} likes auto {}: {}", userId, autoId, result);
        return result;
    }

    @Override
    public Long getAllLikesCountPages(UUID id, UUID brandId) {
        log.info("Calculating pages of liked autos for user {}", id);

        Long countOfLikeByUserId;
        if (brandId == null) {
            countOfLikeByUserId = likeRepository.getCountOfLikeByUserId(id);
        } else {
            countOfLikeByUserId = likeRepository.getCountOfLikeByUserId(id, brandId);
        }

        Long totalPages = countOfLikeByUserId % 10 == 0 ? countOfLikeByUserId / 10 : countOfLikeByUserId / 10 + 1;
        log.info("Total pages of liked autos for user {}: {}", id, totalPages);

        return totalPages;
    }

    public List<Like> findFilteredAndSorted(UUID brand, String sort, String order, Pageable pageable, UUID user) {
        log.info("Filtering and sorting likes for user {} and brand {}", user, brand);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Like> query = cb.createQuery(Like.class);
        Root<Like> root = query.from(Like.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("user").get("id"), user));
        if (brand != null) {
            predicates.add(cb.equal(root.get("auto").get("brand").get("id"), brand));
        }

        Order orderBy = null;
        if (sort != null) {
            switch (sort) {
                case "price":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("auto").get("price")) : cb.asc(root.get("auto").get("price"));
                    break;
                case "mileage":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("auto").get("mileage")) : cb.asc(root.get("auto").get("mileage"));
                    break;
                case "year":
                    orderBy = "desc".equals(order) ? cb.desc(root.get("auto").get("year")) : cb.asc(root.get("auto").get("year"));
                    break;
                default:
                    orderBy = cb.asc(root.get("id")); // По умолчанию
            }
        } else {
            orderBy = cb.desc(root.get("id"));
        }

        query.orderBy(orderBy);
        query.select(root).where(predicates.toArray(new Predicate[0]));

        List<Like> result = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        log.info("Found {} filtered likes", result.size());
        return result;
    }
}
