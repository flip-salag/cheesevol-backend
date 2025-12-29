package com.iucyh.novelservice.novel.service;

import com.iucyh.novelservice.common.exception.DataNotFound;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.exception.DuplicateNovelTitle;
import com.iucyh.novelservice.novel.exception.NovelHasNoCommonEpisodes;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.DeleteNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.novelservice.novel.service.dto.command.UpdateNovelCompletionCommand;
import com.iucyh.novelservice.novel.service.dto.mapper.NovelCommandMapper;
import com.iucyh.novelservice.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.novelservice.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.novelservice.novel.web.dto.response.NovelSaveResponse;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.user.domain.User;
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
    private final EpisodeQueryRepository episodeQueryRepository;

    public NovelSaveResponse createNovel(CreateNovelCommand command) {
        long userId = command.userId();
        User user = findUserById(userId);

        String title = command.title();
        boolean isDuplicateTitle = novelRepository.existsByTitleAndUserId(title, userId);
        if (isDuplicateTitle) {
            throw new DuplicateNovelTitle(title);
        }

        Novel newNovel = NovelCommandMapper.toNovel(command, user);
        Novel savedNovel = novelRepository.save(newNovel);

        return NovelResponseMapper.toNovelSaveResponse(savedNovel);
    }

    public NovelSaveResponse updateNovel(UpdateNovelCommand command) {
        long userId = command.userId();
        String novelPublicId = command.novelPublicId();
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

        return NovelResponseMapper.toNovelSaveResponse(novel);
    }

    public NovelCompletionResponse updateNovelCompletion(UpdateNovelCompletionCommand command) {
        long userId = command.userId();
        String novelPublicId = command.novelPublicId();
        Novel novel = findNovelWithUserId(userId, novelPublicId);

        boolean isCompleted = command.isCompleted();
        if (isCompleted) {
            boolean hasCommonEpisodes = episodeQueryRepository.episodeExistsByNovelIdAndEpisodeType(novel.getId(), EpisodeType.COMMON);
            if (!hasCommonEpisodes) { // 일반 회차가 한개라도 존재하지 않는다면 완결로 변경 불가
                throw new NovelHasNoCommonEpisodes();
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

    public void deleteNovel(DeleteNovelCommand command) {
        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());
        novel.softDelete();
        // 연관된 자식 엔티티 삭제 (bulk update)
        episodeRepository.softDeleteByNovelId(novel.getId(), novel.getDeletedAt());
    }

    private User findUserById(long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(DataNotFound::new);
    }

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new DataNotFound(novelPublicId));
    }
}
