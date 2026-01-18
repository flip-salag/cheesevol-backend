package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class InvalidNovelCursor extends BusinessException {

    public InvalidNovelCursor(Throwable cause) {
        super(NovelErrorCode.INVALID_CURSOR, cause);
    }
}
