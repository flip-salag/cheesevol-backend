package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelAlreadyCompleted extends BusinessException {

    public NovelAlreadyCompleted() {
        super(NovelErrorCode.NOVEL_ALREADY_COMPLETED);
    }
}
