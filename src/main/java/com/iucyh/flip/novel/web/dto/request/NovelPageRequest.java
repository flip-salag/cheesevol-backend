package com.iucyh.flip.novel.web.dto.request;

import com.iucyh.flip.novel.enumtype.NovelSortType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record NovelPageRequest(

        @NotNull
        NovelSortType sort,

        @Size(max = 2048, message = "Next is too long")
        String next,

        @Range(min = 5, max = 50)
        Integer size
) {
    public NovelPageRequest {
        if (size == null) {
            size = 50;
        }
    }
}
