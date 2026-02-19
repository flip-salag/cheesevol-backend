package com.iucyh.novelservice.novel.service.policy;

import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import com.iucyh.novelservice.novel.exception.*;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.repository.custom.paging.cursor.NovelCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelPolicyValidator {

    private final NovelRepository novelRepository;
    private final EpisodeQueryRepository episodeQueryRepository;

    /**
     * <p>디코딩된 {@code cursor} 객체가 전달된 {@code sortType}과 실제로 매칭되는지 검증</p>
     * @param cursor {@code NovelCursor} 객체로 디코딩된 클라이언트로부터 전달받은 커서
     * @param sortType 클라이언트로부터 전달받은 정렬 기준
     * @throws NovelCursorNotMatchesSortType 커서가 해당 정렬 기준과 맞지 않을 때
     */
    public void validateNovelCursorMatchesSortType(NovelCursor cursor, NovelSortType sortType) throws NovelCursorNotMatchesSortType {
        if (cursor.getSortType() != sortType) {
            throw new NovelCursorNotMatchesSortType();
        }
    }

    /**
     * <p>{@code userId}에 해당하는 유저가 작성한 소설 중 전달된 {@code title}과 중복되는 제목을 가진 소설이 없는지 검증</p>
     * <p>삭제된 소설은 제외하고 검증</p>
     * <b>주의: 내부적으로 DB를 조회하므로 Transaction 안에서 실행해야 합니다.</b>
     * @param title 검증할 제목
     * @param userId 기준이 될 유저의 pk
     * @throws DuplicateNovelTitle 중복되는 제목을 가진 소설이 존재할때
     */
    public void validateTitleNotDuplicatedInUserNovels(String title, long userId) throws DuplicateNovelTitle {
        boolean isDuplicated = novelRepository.novelTitleExistsByUserId(title, userId, null);
        if (isDuplicated) {
            throw new DuplicateNovelTitle(title);
        }
    }

    /**
     * <p>{@code userId}에 해당하는 유저가 작성한 소설 중 전달된 {@code title}과 중복되는 제목을 가진 소설이 없는지 검증
     * <br>
     * 업데이트하고 있는 소설을 제외하고 검증해야 할때 등의 상황에서 사용</p>
     * <p>삭제된 소설과 {@code novelPublicId}에 해당하는 소설은 제외하고 검증</p>
     * <b>주의: 내부적으로 DB를 조회하므로 Transaction 안에서 실행해야 합니다.</b>
     * @param title 검증할 제목
     * @param userId 기준이 될 유저의 pk
     * @param novelPublicId 추가로 제외할 소설의 public id
     * @throws DuplicateNovelTitle 중복되는 제목을 가진 소설이 존재할때
     */
    public void validateTitleNotDuplicatedInUserNovels(String title, long userId, String novelPublicId) throws DuplicateNovelTitle {
        boolean isDuplicated = novelRepository.novelTitleExistsByUserId(title, userId, novelPublicId);
        if (isDuplicated) {
            throw new DuplicateNovelTitle(title);
        }
    }

    /**
     * <p>{@code novelId}에 해당하는 소설에 일반(COMMON) 회차가 한개라도 존재하는지 검증</p>
     * <b>주의: 내부적으로 DB를 조회하므로 Transaction 안에서 실행해야 합니다.</b>
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
     * <b>주의: 내부적으로 DB를 조회하므로 Transaction 안에서 실행해야 합니다.</b>
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
