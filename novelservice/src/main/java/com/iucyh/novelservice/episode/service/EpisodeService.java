package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.common.exception.DataNotFound;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.service.dto.mapper.EpisodeCommandMapper;
import com.iucyh.novelservice.episode.service.policy.EpisodePolicyValidator;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSaveResponse;
import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import com.iucyh.novelservice.novel.service.policy.NovelPolicyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeQueryRepository episodeQueryRepository;
    private final EpisodeRepository episodeRepository;

    private final NovelPolicyValidator novelPolicyValidator;
    private final EpisodePolicyValidator episodePolicyValidator;

    public EpisodeSaveResponse createEpisode(CreateEpisodeCommand command) {
        // 순수한 값 검증이므로 불필요한 DB 조회를 방지하기 위해 가장 먼저 검증
        episodePolicyValidator.validateContentLength(command.episodeType(), command.content());

        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());
        novelPolicyValidator.validateNovelNotCompleted(novel); // 완결된 소설은 회차 생성 불가

        return switch (command.episodeType()) {
            case COMMON -> createCommonEpisode(command, novel);
            case PROLOGUE -> createPrologueEpisode(command, novel);
        };
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

    public void deleteEpisode(DeleteEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUserFetch(command.episodePublicId(), command.userId());
        Novel novel = episode.getNovel();

        novelPolicyValidator.validateNovelNotCompleted(novel); // 완결된 소설은 회차 삭제 불가

        episode.softDelete();
        // 삭제될 회차와 삭제된 회차를 제외하고 나머지 회차들 중 가장 최신 회차의 등록일로 Novel의 lastEpisodeAt 업데이트
        // 다음 최신 회차가 없다면 lastEpisodeAt을 null로 설정 (소설 목록 조회 시 소설의 lastEpisodeAt이 null이라면 회차가 없는 것으로 간주)
        LocalDateTime lastEpisodeAt = episodeQueryRepository.findLastEpisodeAtExceptDeletedEpisode(novel.getId(), episode.getPublicId());
        novel.updateLastEpisodeAt(lastEpisodeAt);
    }

    private EpisodeSaveResponse createCommonEpisode(CreateEpisodeCommand command, Novel novel) {
        int newEpisodeNumber = novel.getLastEpisodeNumber() + 1;

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, newEpisodeNumber);
        Episode savedEpisode = episodeRepository.save(episode);
        novel.updateLastEpisode(savedEpisode.getEpisodeNumber(), savedEpisode.getCreatedAt()); // 소설의 마지막 회차와 관련된 컬럼들의 정합성을 위해 최신 회차 기준으로 업데이트

        return EpisodeResponseMapper.toEpisodeSaveResponse(savedEpisode);
    }

    private EpisodeSaveResponse createPrologueEpisode(CreateEpisodeCommand command, Novel novel) {
        novelPolicyValidator.validateNovelHasNoPrologueEpisode(novel.getId()); // 프롤로그는 소설 당 1개만 존재가능

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, 0);
        Episode savedEpisode = episodeRepository.save(episode);

        return EpisodeResponseMapper.toEpisodeSaveResponse(savedEpisode);
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
