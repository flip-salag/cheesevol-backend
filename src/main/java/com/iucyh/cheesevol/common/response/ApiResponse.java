package com.iucyh.cheesevol.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 성공, 에러 공통 응답 객체
 * <p>{@link ErrorInfo}의 {@code causes}는 경우에 따라 {@code null}일 수 있음</p>
 * @param isSuccess 성공 여부
 * @param timestamp 응답이 완료된 시간 ({@link ApiResponse}가 생성된 시점)
 * @param path 요청 경로
 * @param data 응답 데이터 (삭제 요청 등 데이터가 포함되지 않는 요청이거나 에러 응답일 경우 {@code  null})
 * @param error 에러 정보 (성공 응답일 경우 {@code null})
 */
public record ApiResponse<T>(

        @JsonProperty("isSuccess")
        boolean isSuccess,
        LocalDateTime timestamp,
        String path,
        T data,
        ErrorInfo error
) {
    public static <T> ApiResponse<T> success(T data, String path) {
        return new ApiResponse<>(true, now(), path, data, null);
    }

    public static <T> ApiResponse<T> success(String path) {
        return new ApiResponse<>(true, now(), path, null, null);
    }

    public static ApiResponse<Void> error(ErrorInfo error, String path) {
        return new ApiResponse<>(false, now(), path, null, error);
    }

    private static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public record ErrorInfo(String code, String message, Map<String, Object> causes) {

        public static ErrorInfo of(String code, String message, Map<String, Object> causes) {
            return new ErrorInfo(code, message, causes);
        }

        public static ErrorInfo of(String code, String message) {
            return new ErrorInfo(code, message, null);
        }
    }
}
