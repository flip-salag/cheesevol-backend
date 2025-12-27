package com.iucyh.novelservice.episode.service;

import com.iucyh.novelservice.common.vo.HtmlContent;
import com.iucyh.novelservice.episode.constant.EpisodeConstants;
import com.iucyh.novelservice.episode.enumtype.EpisodeType;
import com.iucyh.novelservice.episode.exception.EpisodeContentLengthNotValid;
import com.iucyh.novelservice.episode.exception.EpisodeNotFound;
import com.iucyh.novelservice.episode.repository.query.EpisodeQueryRepository;
import com.iucyh.novelservice.episode.service.dto.command.CreateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.DeleteEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeCommand;
import com.iucyh.novelservice.episode.service.dto.command.UpdateEpisodeContentCommand;
import com.iucyh.novelservice.episode.service.dto.mapper.EpisodeCommandMapper;
import com.iucyh.novelservice.episode.web.dto.response.EpisodeSaveResponse;
import com.iucyh.novelservice.novel.exception.NovelAlreadyCompleted;
import com.iucyh.novelservice.novel.exception.NovelAlreadyHasPrologue;
import com.iucyh.novelservice.novel.exception.NovelNotFound;
import com.iucyh.novelservice.episode.domain.Episode;
import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.episode.web.dto.mapper.EpisodeResponseMapper;
import com.iucyh.novelservice.episode.repository.EpisodeRepository;
import com.iucyh.novelservice.novel.repository.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.iucyh.novelservice.episode.constant.EpisodeConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EpisodeService {

    private final NovelRepository novelRepository;
    private final EpisodeQueryRepository episodeQueryRepository;
    private final EpisodeRepository episodeRepository;

    public EpisodeSaveResponse createEpisode(CreateEpisodeCommand command) {
        Novel novel = findNovelWithUserId(command.userId(), command.novelPublicId());

        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted(); // 완결된 소설은 회차 생성 불가
        }
        validateContentLength(command.episodeType(), command.content());

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
        validateContentLength(episode.getEpisodeType(), command.content());

        episode.updateContent(command.content().getSanitizedValue());
    }

    public void deleteEpisode(DeleteEpisodeCommand command) {
        Episode episode = findEpisodeWithNovelUserFetch(command.episodePublicId(), command.userId());
        Novel novel = episode.getNovel();

        if (novel.isCompletedNovel()) {
            throw new NovelAlreadyCompleted(); // 완결된 소설은 회차 삭제 불가
        }

        episode.softDelete();
        // 삭제될 회차와 삭제된 회차를 제외하고 나머지 회차들 중 가장 최신 회차의 등록일로 Novel의 lastEpisodeAt 업데이트
        // 다음 최신 회차가 없다면 lastEpisodeAt을 null로 설정 (소설 목록 조회 시 소설의 lastEpisodeAt이 null이라면 회차가 없는 것으로 간주)
        LocalDateTime lastEpisodeAt = episodeQueryRepository.findLastEpisodeAtExceptDeletedEpisode(novel.getId(), episode.getPublicId());
        novel.updateLastEpisodeAt(lastEpisodeAt);
    }

    private EpisodeSaveResponse createCommonEpisode(CreateEpisodeCommand command, Novel novel) {
        int newEpisodeNumber = novel.getLastEpisodeNumber() + 1;

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, newEpisodeNumber);
        Episode savedEpisode = episodeRepository.save(episode); // TODO: GlobalExceptionHandler에 DuplicateKeyException 핸들링 메서드 구현 (409)
        novel.updateLastEpisode(savedEpisode.getEpisodeNumber(), savedEpisode.getCreatedAt()); // 소설의 마지막 회차와 관련된 컬럼들의 정합성을 위해 최신 회차 기준으로 업데이트

        return EpisodeResponseMapper.toEpisodeSaveResponse(savedEpisode);
    }

    private EpisodeSaveResponse createPrologueEpisode(CreateEpisodeCommand command, Novel novel) {
        boolean prologueExists = episodeQueryRepository.prologueExistsByNovelId(novel.getId());
        if (prologueExists) { // 프롤로그는 소설 당 1개만 존재가능
            throw new NovelAlreadyHasPrologue(novel.getPublicId());
        }

        Episode episode = EpisodeCommandMapper.toEpisode(command, novel, 0);
        Episode savedEpisode = episodeRepository.save(episode);

        return EpisodeResponseMapper.toEpisodeSaveResponse(savedEpisode);
    }

    /**
     * <p>본문의 길이를 episodeType에 따라 검증</p>
     * @param episodeType 검증의 기준으로 사용할 {@code EpisodeType}
     * @param content 검증할 본문
     * @throws EpisodeContentLengthNotValid 본문의 길이가 너무 길거나 짧을 때
     */
    private void validateContentLength(EpisodeType episodeType, HtmlContent content) {
        int min = 0;
        int max = 0;

        switch (episodeType) {
            case COMMON -> {
                min = COMMON_EPISODE_CONTENT_LENGTH_MIN;
                max = COMMON_EPISODE_CONTENT_LENGTH_MAX;
            }

            case PROLOGUE -> {
                min = PROLOGUE_EPISODE_CONTENT_LENGTH_MIN;
                max = PROLOGUE_EPISODE_CONTENT_LENGTH_MAX;
            }
        }

        String textValue = content.getTextValue();
        boolean isValid = textValue.length() >= min && textValue.length() <= max;
        if (!isValid) {
            throw new EpisodeContentLengthNotValid(episodeType, min, max);
        }
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
