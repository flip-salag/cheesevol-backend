package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelHasNoCommonEpisodes extends ServiceException {

    public NovelHasNoCommonEpisodes() {
        super(NovelErrorCode.NOVEL_HAS_NO_COMMON_EPISODES);
    }
}
