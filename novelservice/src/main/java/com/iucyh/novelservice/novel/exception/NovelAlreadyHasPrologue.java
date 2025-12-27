package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

import java.util.Map;

public class NovelAlreadyHasPrologue extends ServiceException {

    public NovelAlreadyHasPrologue() {
        super(NovelErrorCode.NOVEL_ALREADY_HAS_PROLOGUE);
    }
}
