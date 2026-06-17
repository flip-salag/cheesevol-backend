package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

public class NovelAlreadyCompleted extends BusinessException {

    public NovelAlreadyCompleted() {
        super(NovelErrorCode.NOVEL_ALREADY_COMPLETED);
    }
}
