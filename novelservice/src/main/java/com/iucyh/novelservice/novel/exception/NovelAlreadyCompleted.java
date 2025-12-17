package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelAlreadyCompleted extends ServiceException {

    public NovelAlreadyCompleted() {
        super(NovelErrorCode.NOVEL_ALREADY_COMPLETED);
    }
}
