package com.iucyh.flip.novel.repository.custom.paging.cursor;

import com.iucyh.flip.novel.enumtype.NovelSortType;

/**
 * <p>Novel의 페이징에서 사용되는 커서들의 기본 타입</p>
 * <b>모든 Novel 페이징 커서 클래스들은 이 인터페이스를 구현해야 합니다.</b>
 */
public interface NovelCursor {

    /**
     * <p>커서가 대표하는 정렬 기준(e.g. NovelPopularCursor -> NovelSortType.POPULAR)</p>
     * <p>* 각 커서 클래스에서 구현 시 정렬 기준들만 인자로 받는 정적 팩토리 메서드(of)를 구현하고, sortType 필드는 내부적으로 직접 설정하도록 하는 것을 권장(외부에서 받으면 잘못된 NovelSortType이 전달될 가능성이 있으므로)</p>
     */
    NovelSortType getSortType();
}
