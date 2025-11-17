package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.novel.exception.DuplicateNovelTitle;
import com.iucyh.novelservice.novel.exception.HasNoEpisodes;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelCategory;
import com.iucyh.novelservice.novel.service.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.service.command.UpdateNovelCommand;
import com.iucyh.novelservice.novel.service.command.mapper.NovelCommandMapper;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelRequestMapper;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.request.UpdateNovelRequest;
import com.iucyh.novelservice.novel.web.dto.response.NovelResponse;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.user.domain.User;
import com.iucyh.novelservice.user.service.reader.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final UserReader userReader;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    public NovelResponse createNovel(CreateNovelCommand command, long userId) {
        User user = userReader.findUserById(userId);
        String title = command.title();

        boolean isDuplicateTitle = novelRepository.existsByTitleAndUserIdAndDeletedAtIsNull(title, userId);
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
            boolean isDuplicateTitle = novelRepository.existsByTitleAndUserIdAndPublicIdNotAndDeletedAtIsNull(title, userId, novelPublicId);
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
                throw new HasNoEpisodes();
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

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new NovelNotFound(novelPublicId));
    }
}
