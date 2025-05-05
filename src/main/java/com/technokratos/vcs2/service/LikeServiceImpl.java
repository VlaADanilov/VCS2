package com.technokratos.vcs2.service;

import com.technokratos.vcs2.exception.LikeException;
import com.technokratos.vcs2.exception.notFound.AutoNotFoundException;
import com.technokratos.vcs2.exception.notFound.UserNotFoundException;
import com.technokratos.vcs2.mapper.AutoMapper;
import com.technokratos.vcs2.model.dto.response.ListElementAutoResponseDto;
import com.technokratos.vcs2.model.entity.Auto;
import com.technokratos.vcs2.model.entity.Like;
import com.technokratos.vcs2.repository.AutoRepository;
import com.technokratos.vcs2.repository.LikeRepository;
import com.technokratos.vcs2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AutoRepository autoRepository;
    private final AutoMapper autoMapper;
    private final EntityManager entityManager;
    @Override
    public List<ListElementAutoResponseDto> getAutoWhoUserLike(UUID userId, int page, int size, String sort, String order, UUID brand_id) {
        List<Like> allByUser = findFilteredAndSorted(brand_id,sort,order,
                PageRequest.of(page, size), userId);
        return
                allByUser.stream().map((a) -> autoMapper.toListElementAutoResponseDto(a.getAuto())).toList();
    }

    @Override
    public void addLike(UUID userId, UUID autoId) {
        checkUserAndAuto(userId, autoId);
        if (likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId))) {
            throw new LikeException("like already exists");
        }
        Like like = new Like();
        like.setUser(userRepository.getReferenceById(userId));
        like.setAuto(autoRepository.getReferenceById(autoId));
        like.setId(UUID.randomUUID());
        likeRepository.save(like);
    }

    private void checkUserAndAuto(UUID userId, UUID autoId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!autoRepository.existsById(autoId)) {
            throw new AutoNotFoundException(autoId);
        }
    }

    @Override
    @Transactional
    public void removeLike(UUID userId, UUID autoId) {
        checkUserAndAuto(userId, autoId);
        if (!likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId))) {
            throw new LikeException("like not exists");
        }
        likeRepository.deleteByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId));
    }

    @Override
    public boolean existLike(UUID userId, UUID autoId) {
        return likeRepository.existsByUserAndAuto(
                userRepository.getReferenceById(userId),
                autoRepository.getReferenceById(autoId)
        );
    }

    @Override
    public Long getAllLikesCountPages(UUID id, UUID brandId) {
        Long countOfLikeByUserId;
        if (brandId == null) {
            countOfLikeByUserId  = likeRepository.getCountOfLikeByUserId(id);
        } else {
            countOfLikeByUserId = likeRepository.getCountOfLikeByUserId(id, brandId);
        }
        return countOfLikeByUserId % 10 == 0? countOfLikeByUserId / 10 : countOfLikeByUserId / 10 + 1;
    }

    public List<Like> findFilteredAndSorted(UUID brand, String sort, String order, Pageable pageable, UUID user) {
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
                    orderBy = cb.asc(root.get("id"));
            }
        } else {
            orderBy = cb.desc(root.get("id"));
        }
        query.orderBy(orderBy);

        query.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
