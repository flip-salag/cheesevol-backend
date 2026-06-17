package com.iucyh.flip.novel.service;

import com.iucyh.flip.common.exception.DataNotFound;
import com.iucyh.flip.episode.repository.EpisodeRepository;
import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.repository.NovelRepository;
import com.iucyh.flip.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.DeleteNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCompletionCommand;
import com.iucyh.flip.novel.service.dto.mapper.NovelCommandMapper;
import com.iucyh.flip.novel.service.policy.NovelPolicyValidator;
import com.iucyh.flip.novel.web.dto.mapper.NovelResponseMapper;
import com.iucyh.flip.novel.web.dto.response.NovelCompletionResponse;
import com.iucyh.flip.novel.web.dto.response.NovelLikeCountResponse;
import com.iucyh.flip.novel.web.dto.response.NovelSaveResponse;
import com.iucyh.flip.user.domain.User;
import com.iucyh.flip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class NovelService {

    private final UserRepository userRepository;
    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    private final NovelPolicyValidator novelPolicyValidator;

    public NovelSaveResponse createNovel(CreateNovelCommand command) {
        long userId = command.userId();
        User user = findUserById(userId);

        // 같은 작가의 소설 중 중복되는 제목이 있다면 소설 생성 불가
        novelPolicyValidator.validateTitleNotDuplicatedInUserNovels(command.title(), userId);

        Novel newNovel = NovelCommandMapper.toNovel(command, user, LocalDateTime.now());
        Novel savedNovel = novelRepository.save(newNovel);

        return NovelResponseMapper.toNovelSaveResponse(savedNovel);
    }

    public NovelSaveResponse updateNovel(UpdateNovelCommand command) {
        long userId = command.userId();
        String novelPublicId = command.novelPublicId();
        Novel novel = findNovelWithUserId(userId, novelPublicId);

        if (command.title() != null) {
            // 같은 작가의 소설 중 중복되는 제목이 있다면 소설 업데이트 불가 (업데이트 중인 소설은 제외하고 검증)
            novelPolicyValidator.validateTitleNotDuplicatedInUserNovels(command.title(), userId, novelPublicId);
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
            // 일반 회차가 한개도 존재하지 않는다면 완결로 변경 불가
            novelPolicyValidator.validateNovelHasCommonEpisodes(novel.getId());
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
