package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class NovelHasNoEpisodes extends ServiceException {

    public NovelHasNoEpisodes() {
        super(NovelErrorCode.NOVEL_HAS_NO_EPISODES);
    }
}
