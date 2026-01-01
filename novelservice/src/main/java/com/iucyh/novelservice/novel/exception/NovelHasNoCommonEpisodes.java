package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.base.exception.BusinessException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelHasNoCommonEpisodes extends BusinessException {

    public NovelHasNoCommonEpisodes() {
        super(NovelErrorCode.NOVEL_HAS_NO_COMMON_EPISODES);
    }
}
