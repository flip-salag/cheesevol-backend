package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.novel.exception.DuplicateNovelTitle;
import com.iucyh.novelservice.novel.exception.NovelHasNoEpisodes;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.mapper.NovelCommandMapper;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.exception.UserNotFound;
import com.iucyh.novelservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final UserRepository userRepository;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    public NovelResponse createNovel(CreateNovelCommand command, long userId) {
        User user = findUserById(userId);
        String title = command.title();

        boolean isDuplicateTitle = novelRepository.existsByTitleAndUserId(title, userId);
        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = NovelCommandMapper.toNovel(command, user);
        Novel savedNovel = novelRepository.save(newNovel);

        return NovelResponseMapper.toNovelResponse(savedNovel);
    }

    public NovelResponse updateNovel(UpdateNovelCommand command, long userId, String novelPublicId) {
        Novel novel = findNovelWithUserId(userId, novelPublicId);

        if (command.title() != null) {
            String title = command.title();
            boolean isDuplicateTitle = novelRepository.existsByTitleAndUserIdAndPublicIdNot(title, userId, novelPublicId);
            if (isDuplicateTitle) {
                throw new DuplicateNovelTitle(title);
            }
        }

        novel.updateTextMetaData(command.title(), command.description());
        novel.updateCategory(command.category());

        return NovelResponseMapper.toNovelResponse(novel);
    }

    public NovelCompletionResponse updateNovelCompletion(boolean isCompleted, long userId, String novelPublicId) {
        Novel novel = findNovelWithUserId(userId, novelPublicId);

        if (isCompleted) {
            boolean hasEpisodes = episodeRepository.existsByNovelIdAndDeletedAtIsNull(novel.getId());
            if (!hasEpisodes) {
                throw new NovelHasNoEpisodes();
            }
        }

        novel.updateCompletion(isCompleted);
        return NovelResponseMapper.toNovelCompletionResponse(novel);
    }

    public NovelLikeCountResponse addLikeCount(long userId, long novelId) {
        //Novel novel = findNovelWithUserId(userId, novelId);
        //novel.addLikes(1);

        return null;
    }

    public NovelLikeCountResponse removeLikeCount(long userId, long novelId) {
        //Novel novel = findNovelWithUserId(userId, novelId);
        //novel.removeLikes(1);

        return null;
    }

    public void deleteNovel(long userId, String novelPublicId) {
        Novel novel = findNovelWithUserId(userId, novelPublicId);
        novel.softDelete();
    }

    private User findUserById(long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserNotFound(userId));
    }

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new NovelNotFound(novelPublicId));
    }
}
