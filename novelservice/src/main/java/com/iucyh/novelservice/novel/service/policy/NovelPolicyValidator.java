package com.iucyh.novelservice.novel.service.policy;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.exception.DuplicateNovelTitle;
import com.iucyh.novelservice.novel.exception.NovelAlreadyCompleted;
import com.iucyh.novelservice.novel.exception.NovelAlreadyHasPrologue;
import com.iucyh.novelservice.novel.exception.NovelHasNoCommonEpisodes;
import com.iucyh.novelservice.novel.repository.query.NovelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelPolicyValidator {

    private final NovelQueryRepository novelQueryRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    /**
     * <p>{@code userId}에 해당하는 유저가 작성한 소설 중 전달된 {@code title}과 중복되는 제목을 가진 소설이 없는지 검증</p>
     * <b>삭제된 소설은 제외하고 검증</b>
     * @param title 검증할 제목
     * @param userId 기준이 될 유저의 pk
     * @throws DuplicateNovelTitle 중복되는 제목을 가진 소설이 존재할때
     */
    public void validateTitleNotDuplicatedInUserNovels(String title, long userId) throws DuplicateNovelTitle {
        boolean isDuplicated = novelQueryRepository.novelTitleExistsByUserId(title, userId, null);
        if (isDuplicated) {
            throw new DuplicateNovelTitle(title);
        }
    }

    /**
     * <p>{@code userId}에 해당하는 유저가 작성한 소설 중 전달된 {@code title}과 중복되는 제목을 가진 소설이 없는지 검증</p>
     * <p>업데이트하고 있는 소설을 제외하고 검증해야 할때 등의 상황에서 사용</p>
     * <b>삭제된 소설과 {@code novelPublicId}에 해당하는 소설은 제외하고 검증</b>
     * @param title 검증할 제목
     * @param userId 기준이 될 유저의 pk
     * @param novelPublicId 추가로 제외할 소설의 public id
     * @throws DuplicateNovelTitle 중복되는 제목을 가진 소설이 존재할때
     */
    public void validateTitleNotDuplicatedInUserNovels(String title, long userId, String novelPublicId) throws DuplicateNovelTitle {
        boolean isDuplicated = novelQueryRepository.novelTitleExistsByUserId(title, userId, novelPublicId);
        if (isDuplicated) {
            throw new DuplicateNovelTitle(title);
        }
    }

    /**
     * <p>{@code novelId}에 해당하는 소설에 일반(COMMON) 회차가 한개라도 존재하는지 검증</p>
     * @param novelId 검증할 소설의 pk
     * @throws NovelHasNoCommonEpisodes 해당 소설에 일반 회차가 한개도 존재하지 않을때
     */
    public void validateNovelHasCommonEpisodes(long novelId) throws NovelHasNoCommonEpisodes {
        boolean hasCommonEpisode = episodeQueryRepository.episodeExistsByNovelIdAndEpisodeType(novelId, EpisodeType.COMMON);
        if (!hasCommonEpisode) {
            throw new NovelHasNoCommonEpisodes();
        }
    }

    /**
     * <p>{@code novelId}에 해당하는 소설에 프롤로그 회차가 존재하지 않는지 검증</p>
     * @param novelId 검증할 소설의 pk
     * @throws NovelAlreadyHasPrologue 해당 소설에 프롤로그 회차가 존재할때
     */
    public void validateNovelHasNoPrologueEpisode(long novelId) throws NovelAlreadyHasPrologue {
        boolean hasPrologueEpisode = episodeQueryRepository.episodeExistsByNovelIdAndEpisodeType(novelId, EpisodeType.PROLOGUE);
        if (hasPrologueEpisode) {
            throw new NovelAlreadyHasPrologue();
        }
    }

    /**
     * <p>소설이 완결되지 않았는지 검증</p>
     * @param novel 검증할 소설의 엔티티
     * @throws NovelAlreadyCompleted 소설이 완결되었을 때
     */
    public void validateNovelNotCompleted(Novel novel) throws NovelAlreadyCompleted {
        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted();
        }
    }
}
