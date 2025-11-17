package com.iucyh.novelservice.novel.exception;

import com.iucyh.novelservice.common.exception.ServiceException;
import com.iucyh.novelservice.novel.exception.errorcode.NovelErrorCode;

public class HasNoEpisodes extends ServiceException {

    public HasNoEpisodes() {
        super(NovelErrorCode.HAS_NO_EPISODES);
    }
}
