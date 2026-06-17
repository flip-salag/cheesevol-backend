package com.iucyh.flip.base.response.api;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private final Boolean isSuccess = true;
    private final T data;

    protected SuccessResponse(T data) {
        this.data = data;
    }
}
