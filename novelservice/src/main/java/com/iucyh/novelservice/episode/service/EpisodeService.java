package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.episode.exception.EpisodeNotFound;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.service.dto.mapper.EpisodeCommandMapper;
import com.iucyh.novelservice.novel.exception.NovelAlreadyCompleted;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSummaryResponse;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
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

    public EpisodeSummaryResponse createEpisode(CreateEpisodeCommand command) {
        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());
        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted(); // 완결된 소설은 회차 생성 불가
        }

        int newEpisodeNumber = novel.getLastEpisodeNumber() + 1;
        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, newEpisodeNumber);
        Episode savedEpisode = episodeRepository.save(episode); // TODO: GlobalExceptionHandler에 DuplicateKeyException 핸들링 메서드 구현 (409)
        novel.updateLastEpisode(savedEpisode.getEpisodeNumber(), savedEpisode.getCreatedAt()); // 소설의 마지막 회차와 관련된 컬럼들의 정합성을 위해 최신 회차 기준으로 업데이트

        return EpisodeResponseMapper.toEpisodeSummaryResponse(savedEpisode);
    }

    public EpisodeSummaryResponse updateEpisode(UpdateEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUser(command.episodePublicId(), command.userId());
        episode.updateTextMetaData(command.title(), command.description());

        return EpisodeResponseMapper.toEpisodeSummaryResponse(episode);
    }

    public void updateEpisodeContent(UpdateEpisodeContentCommand command) {
        Episode episode = findEpisodeWithNovelUser(command.episodePublicId(), command.userId());
        episode.updateContent(command.content());
    }

    public void deleteEpisode(DeleteEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUserFetch(command.episodePublicId(), command.userId());
        Novel novel = episode.getNovel();

        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted(); // 완결된 소설은 회차 삭제 불가
        }

        episode.softDelete();
        // 삭제될 회차와 삭제된 회차를 제외하고 나머지 회차들 중 가장 최신회차의 등록일로 Novel의 lastEpisodeAt 업데이트
        // 다음 최신 회차가 없다면 lastEpisodeAt을 null로 설정 (소설 목록 조회 시 소설의 lastEpisodeAt이 null이라면 회차가 없는 것으로 간주)
        LocalDateTime lastEpisodeAt = episodeQueryRepository.findLastEpisodeAtExceptDeletedEpisode(novel.getId(), episode.getPublicId());
        novel.updateLastEpisodeAt(lastEpisodeAt);
    }

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new NovelNotFound(novelPublicId));
    }

    private Episode findEpisodeWithNovelUser(String episodePublicId, long userId) {
        return episodeRepository.findByPublicIdWithNovelUser(episodePublicId, userId)
                .orElseThrow(() -> new EpisodeNotFound(episodePublicId));
    }

    private Episode findEpisodeWithNovelUserFetch(String episodePublicId, long userId) {
        return episodeRepository.findByPublicIdWithNovelUserFetch(episodePublicId, userId)
                .orElseThrow(() -> new EpisodeNotFound(episodePublicId));
    }
}
