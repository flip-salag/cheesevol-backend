package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelAlreadyHasPrologue extends BusinessException {

    public NovelAlreadyHasPrologue() {
        super(NovelErrorCode.NOVEL_ALREADY_HAS_PROLOGUE);
    }
}
