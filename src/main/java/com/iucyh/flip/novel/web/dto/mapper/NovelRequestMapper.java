package com.iucyh.flip.novel.web.dto.mapper;

import com.iucyh.flip.novel.enumtype.NovelCategory;
import com.iucyh.flip.novel.service.dto.command.CreateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.DeleteNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCommand;
import com.iucyh.flip.novel.service.dto.command.UpdateNovelCompletionCommand;
import com.iucyh.flip.novel.service.dto.query.GetNewNovelsQuery;
import com.iucyh.flip.novel.service.dto.query.GetNovelsQuery;
import com.iucyh.flip.novel.web.dto.request.CreateNovelRequest;
import com.iucyh.flip.novel.web.dto.request.NovelPageRequest;
import com.iucyh.flip.novel.web.dto.request.UpdateNovelCompletionRequest;
import com.iucyh.flip.novel.web.dto.request.UpdateNovelRequest;

public class NovelRequestMapper {

    private NovelRequestMapper() {}

    public static CreateNovelCommand toCreateNovelCommand(CreateNovelRequest request, long userId) {
        return new CreateNovelCommand(
                userId,
                request.title(),
                request.description(),
                NovelCategory.of(request.category())
        );
    }

    public static UpdateNovelCommand toUpdateNovelCommand(UpdateNovelRequest request, long userId, String novelPublicId) {
        return new UpdateNovelCommand(
                userId,
                novelPublicId,
                request.title(),
                request.description(),
                NovelCategory.of(request.category())
        );
    }

    public static UpdateNovelCompletionCommand toUpdateNovelCompletionCommand(UpdateNovelCompletionRequest request, long userId, String novelPublicId) {
        return new UpdateNovelCompletionCommand(
                userId,
                novelPublicId,
                request.isCompleted()
        );
    }

    public static DeleteNovelCommand toDeleteNovelCommand(long userId, String novelPublicId) {
        return new DeleteNovelCommand(userId, novelPublicId);
    }

    public static GetNovelsQuery toGetNovelsQuery(NovelPageRequest request, NovelCategory category) {
        return new GetNovelsQuery(
                category,
                request.sort(),
                request.next(),
                request.size()
        );
    }

    public static GetNewNovelsQuery toGetNewNovelsQuery(NovelPageRequest request, NovelCategory category) {
        return new GetNewNovelsQuery(
                category,
                request.sort(),
                request.next(),
                request.size()
        );
    }
}
