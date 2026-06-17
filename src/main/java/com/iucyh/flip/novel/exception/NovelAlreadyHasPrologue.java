package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

public class NovelAlreadyHasPrologue extends BusinessException {

    public NovelAlreadyHasPrologue() {
        super(NovelErrorCode.NOVEL_ALREADY_HAS_PROLOGUE);
    }
}
