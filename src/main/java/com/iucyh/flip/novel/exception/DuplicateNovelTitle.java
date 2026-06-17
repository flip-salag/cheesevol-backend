package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

import java.util.Map;

public class DuplicateNovelTitle extends BusinessException {

    public DuplicateNovelTitle(String title) {
        super(
                NovelErrorCode.DUPLICATE_TITLE,
                Map.of("title", title)
        );
    }
}
