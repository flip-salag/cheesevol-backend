package com.iucyh.flip.novel.exception.errorcode;

import com.iucyh.flip.base.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NovelErrorCode implements ErrorCode {

    INVALID_CURSOR(HttpStatus.BAD_REQUEST, "NOVEL-4001", "Invalid paging cursor"),
    CURSOR_NOT_MATCHES_SORT_TYPE(HttpStatus.BAD_REQUEST, "NOVEL-4002", "Cursor doesn't match sort type"),

    NOVEL_ALREADY_COMPLETED(HttpStatus.CONFLICT, "NOVEL-4091", "Novel already completed"),
    DUPLICATE_TITLE(HttpStatus.CONFLICT, "NOVEL-4092", "Novel title already exists"),
    NOVEL_HAS_NO_COMMON_EPISODES(HttpStatus.CONFLICT, "NOVEL-4093", "Novel doesn't have common episodes"),
    NOVEL_ALREADY_HAS_PROLOGUE(HttpStatus.CONFLICT, "NOVEL-4094", "Novel already has prologue");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
