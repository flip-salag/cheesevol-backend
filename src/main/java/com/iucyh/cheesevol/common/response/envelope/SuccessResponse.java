package com.iucyh.cheesevol.common.response.envelope;

public record SuccessResponse<T>(Boolean isSuccess, T data) {

    private SuccessResponse(T data) {
        this(true, data);
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }

    public static SuccessResponse<Void> empty() {
        return new SuccessResponse<>(null);
    }
}
