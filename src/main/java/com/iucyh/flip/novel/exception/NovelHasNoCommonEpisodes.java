package com.iucyh.flip.novel.exception;

import com.iucyh.flip.base.exception.BusinessException;
import com.iucyh.flip.novel.exception.errorcode.NovelErrorCode;

public class NovelHasNoCommonEpisodes extends BusinessException {

    public NovelHasNoCommonEpisodes() {
        super(NovelErrorCode.NOVEL_HAS_NO_COMMON_EPISODES);
    }
}
