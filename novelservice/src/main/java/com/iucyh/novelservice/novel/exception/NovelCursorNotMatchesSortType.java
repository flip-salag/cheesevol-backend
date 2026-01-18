package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelCursorNotMatchesSortType extends BusinessException {

    public NovelCursorNotMatchesSortType() {
        super(NovelErrorCode.CURSOR_NOT_MATCHES_SORT_TYPE);
    }
}
