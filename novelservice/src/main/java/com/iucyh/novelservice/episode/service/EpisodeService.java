package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.episode.exception.EpisodeNotFound;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
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

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeRepository episodeRepository;

    public EpisodeSummaryResponse createEpisode(CreateEpisodeCommand command) {
        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());
        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted();
        }

        int newEpisodeNumber = novel.getLastEpisodeNumber() + 1;
        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, newEpisodeNumber);
        Episode savedEpisode = episodeRepository.save(episode); // TODO: GlobalExceptionHandler에 DuplicateKeyException 핸들링 메서드 구현 (409)
        novel.updateLastEpisode(savedEpisode.getEpisodeNumber(), savedEpisode.getCreatedAt());

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

    public void deleteEpisode(long novelId, long episodeId) {
        Episode episode = findEpisodeWithNovelId(novelId, episodeId);
        episode.softDelete();
    }

    private Novel findNovelWithUserId(long userId, String novelPublicId) {
        return novelRepository.findByUserIdAndPublicIdAndDeletedAtIsNull(userId, novelPublicId)
                .orElseThrow(() -> new NovelNotFound(novelPublicId));
    }

    private Episode findEpisodeWithNovelUser(String episodePublicId, long userId) {
        return episodeRepository.findByPublicIdWithNovelUser(episodePublicId, userId)
                .orElseThrow(() -> new EpisodeNotFound(episodePublicId));
    }

    private Episode findEpisodeWithNovelId(long novelId, long episodeId) {
        return episodeRepository.findByIdAndNovelId(episodeId, novelId)
                .orElseThrow(() -> new EpisodeNotFound(String.valueOf(episodeId))); // 임시 캐스팅, 수정 혹은 삭제 예정
    }
}
