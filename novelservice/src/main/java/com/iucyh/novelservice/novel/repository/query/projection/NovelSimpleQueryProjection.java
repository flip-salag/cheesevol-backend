package com.iucyh.novelservice.novel.repository.query.projection;

import com.iucyh.novelservice.novel.domain.Novel;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class NovelSimpleQueryProjection implements NovelQueryProjection {

    private final Novel novel;

    @QueryProjection
    public NovelSimpleQueryProjection(Novel novel) {
        this.novel = novel;
    }
}
