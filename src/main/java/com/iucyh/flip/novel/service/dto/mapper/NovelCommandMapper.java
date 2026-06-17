package com.iucyh.flip.novel.service.dto.mapper;

import com.iucyh.flip.novel.domain.Novel;
import com.iucyh.flip.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.flip.user.domain.User;

import java.time.LocalDateTime;

public class NovelCommandMapper {

    private NovelCommandMapper() {}

    public static Novel toNovel(CreateNovelCommand command, User user, LocalDateTime publishedAt) {
        return Novel.of(
                command.title(),
                command.description(),
                command.category(),
                publishedAt,
                user
        );
    }
}
