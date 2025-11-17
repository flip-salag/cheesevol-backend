package com.iucyh.novelservice.novel.service.dto.mapper;

import com.iucyh.novelservice.novel.domain.Novel;
import com.iucyh.novelservice.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.novelservice.user.domain.User;

public class NovelCommandMapper {

    private NovelCommandMapper() {}

    public static Novel toNovel(CreateNovelCommand command, User user) {
        return Novel.of(
                command.title(),
                command.description(),
                command.category(),
                user
        );
    }
}
