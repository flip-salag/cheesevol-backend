package com.iucyh.flip.episode.service;

import com.iucyh.flip.common.exception.DataNotFound;
import com.iucyh.flip.episode.domain.Episode;
import com.iucyh.flip.episode.enumtype.EpisodeType;
import com.iucyh.flip.episode.repository.EpisodeRepository;
import com.iucyh.flip.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.flip.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.flip.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.flip.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.flip.episode.service.dto.mapper.EpisodeCommandMapper;
import com.iucyh.flip.episode.service.policy.EpisodePolicyValidator;
import com.iucyh.flip.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.flip.episode.web.dto.response.EpisodeSaveResponse;
import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.repository.NovelRepository;
import com.iucyh.flip.novel.service.policy.NovelPolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    private final NovelPolicyValidator novelPolicyValidator;
    private final EpisodePolicyValidator episodePolicyValidator;

    public EpisodeSaveResponse createEpisode(CreateEpisodeCommand command) {
        // 순수한 값 검증이므로 불필요한 DB 조회를 방지하기 위해 가장 먼저 검증
        episodePolicyValidator.validateContentLength(command.episodeType(), command.content());

        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());
        novelPolicyValidator.validateNovelNotCompleted(novel); // 완결된 소설은 회차 생성 불가

        Episode savedEpisode = switch (command.episodeType()) {
            case COMMON -> createCommonEpisode(command, novel);
            case PROLOGUE -> createPrologueEpisode(command, novel);
        };
        LocalDate lastEpisodePublishDate = toLastEpisodePublishDate(savedEpisode.getPublishedAt());
        novel.updateLastEpisodePublishDate(lastEpisodePublishDate);

        // COMMON 회차 생성 시 관련 컬럼 업데이트
        if (command.episodeType() == EpisodeType.COMMON) {
            novel.updateMaxEpisodeNumber(savedEpisode.getEpisodeNumber());
            novel.increaseCommonEpisodeCount();
        }

        return EpisodeResponseMapper.toEpisodeSaveResponse(savedEpisode);
    }

    public EpisodeSaveResponse updateEpisode(UpdateEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUser(command.episodePublicId(), command.userId());
        episode.updateTextMetaData(command.title(), command.description());

        return EpisodeResponseMapper.toEpisodeSaveResponse(episode);
    }

    public void updateEpisodeContent(UpdateEpisodeContentCommand command) {
        Episode episode = findEpisodeWithNovelUser(command.episodePublicId(), command.userId());
        episodePolicyValidator.validateContentLength(episode.getEpisodeType(), command.content());

        episode.updateContent(command.content().getSanitizedValue());
    }

    // TODO: 락 예외 발생 시 재시도 기능 구현
    public void deleteEpisode(DeleteEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUserFetch(command.episodePublicId(), command.userId());
        Novel novel = episode.getNovel();

        novelPolicyValidator.validateNovelNotCompleted(novel); // 완결된 소설은 회차 삭제 불가
        episode.softDelete();

        // 가장 최신 회차의 발행일시를 가장 최신 회차 발행일로 변환 후 novel에 저장 (최신 회차 발행일시 조회 시 삭제중인 회차, 삭제된 회차는 제외)
        LocalDateTime lastEpisodePublishedAt = episodeRepository.findLastEpisodePublishedAt(novel.getId(), episode.getId());
        LocalDate lastEpisodePublishDate = toLastEpisodePublishDate(lastEpisodePublishedAt);

        // 소설에 더 이상 회차가 한개도 존재하지 않으면 자연스럽게 null로 설정
        novel.updateLastEpisodePublishDate(lastEpisodePublishDate);

        // COMMON 회차와 관련된 컬럼 업데이트 (삭제하려는 회차가 COMMON이 아니라면 COMMON과 관련된 상태는 변하지 않으므로 업데이트 스킵)
        if (episode.getEpisodeType() == EpisodeType.COMMON) {
            novel.decreaseCommonEpisodeCount();
        }
    }

    private Episode createCommonEpisode(CreateEpisodeCommand command, Novel novel) {
        int newEpisodeNumber = novel.generateNewEpisodeNumber();

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, newEpisodeNumber, LocalDateTime.now());
        return episodeRepository.save(episode);
    }

    private Episode createPrologueEpisode(CreateEpisodeCommand command, Novel novel) {
        novelPolicyValidator.validateNovelHasNoPrologueEpisode(novel.getId()); // 프롤로그는 소설 당 1개만 존재가능

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, 0, LocalDateTime.now());
        return episodeRepository.save(episode);
    }

    /**
     * <p>전달받은 {@code LocalDateTime} 타입의 회차 발행일시를 {@code LocalDate} 타입의 마지막 회차 발행일로 변환</p>
     * @param episodePublishedAt 변환할 회차의 발행일시
     * @return 변환된 결과, episodePublishedAt이 null이라면 그 값 그대로 반환
     */
    private LocalDate toLastEpisodePublishDate(LocalDateTime episodePublishedAt) {
        return episodePublishedAt == null ? null : episodePublishedAt.toLocalDate();
    }

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new DataNotFound(novelPublicId));
    }

    private Episode findEpisodeWithNovelUser(String episodePublicId, long userId) {
        return episodeRepository.findByPublicIdWithNovelUser(episodePublicId, userId)
                .orElseThrow(() -> new DataNotFound(episodePublicId));
    }

    private Episode findEpisodeWithNovelUserFetch(String episodePublicId, long userId) {
        return episodeRepository.findByPublicIdWithNovelUserFetch(episodePublicId, userId)
                .orElseThrow(() -> new DataNotFound(episodePublicId));
    }
}
