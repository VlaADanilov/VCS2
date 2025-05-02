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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final AutoRepository autoRepository;
    private final AutoMapper autoMapper;
    @Override
    public List<ListElementAutoResponseDto> getAutoWhoUserLike(UUID userId, int page, int size) {
        List<Like> allByUser = likeRepository.findAllByUser(userRepository.getReferenceById(userId),
                PageRequest.of(page, size));
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
    public Long getAllLikesCountPages(UUID id) {
        Long countOfLikeByUserId = likeRepository.getCountOfLikeByUserId(id);
        return countOfLikeByUserId % 10 == 0? countOfLikeByUserId / 10 : countOfLikeByUserId / 10 + 1;
    }
}
