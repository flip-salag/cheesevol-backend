package com.iucyh.novelservice.novel.web.dto.request;

import com.iucyh.novelservice.novel.enumtype.NovelSortType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record NovelPageRequest(

        @NotNull
        NovelSortType sort,

        @Size(max = 2048, message = "Cursor length is too long")
        String cursor,

        @Range(min = 5, max = 50, message = "Size must be between 5 and 50")
        Integer size
) {
    public NovelPageRequest {
        if (size == null) {
            size = 50;
        }
    }
}
