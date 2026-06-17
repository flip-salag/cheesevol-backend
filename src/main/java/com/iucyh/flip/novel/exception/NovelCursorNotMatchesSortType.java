package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

public class NovelCursorNotMatchesSortType extends BusinessException {

    public NovelCursorNotMatchesSortType() {
        super(NovelErrorCode.CURSOR_NOT_MATCHES_SORT_TYPE);
    }
}
