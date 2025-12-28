package com.iucyh.novelservice.novel.exception.errorcode;

import com.iucyh.novelservice.common.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NovelErrorCode implements ErrorCode {

    NOVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "NOVEL-4041", "Novel not found with this id"),

    INVALID_CURSOR(HttpStatus.BAD_REQUEST, "NOVEL-4001", "Invalid paging cursor"),

    NOVEL_ALREADY_COMPLETED(HttpStatus.CONFLICT, "NOVEL-4091", "Novel already completed"),
    DUPLICATE_TITLE(HttpStatus.CONFLICT, "NOVEL-4092", "Novel title already exists"),
    NOVEL_HAS_NO_COMMON_EPISODES(HttpStatus.CONFLICT, "NOVEL-4093", "Novel doesn't have common episodes"),
    NOVEL_ALREADY_HAS_PROLOGUE(HttpStatus.CONFLICT, "NOVEL-4094", "Novel already has prologue");

    private final HttpStatus status;
    private final String code;
    private final String defaultMessage;
}
