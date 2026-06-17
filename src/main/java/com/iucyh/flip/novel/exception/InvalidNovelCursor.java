package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

public class InvalidNovelCursor extends BusinessException {

    public InvalidNovelCursor(Throwable cause) {
        super(NovelErrorCode.INVALID_CURSOR, cause);
    }
}
