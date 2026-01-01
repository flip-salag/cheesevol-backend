package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

import java.util.Map;

public class DuplicateNovelTitle extends BusinessException {

    public DuplicateNovelTitle(String title) {
        super(
                NovelErrorCode.DUPLICATE_TITLE,
                Map.of("title", title)
        );
    }
}
