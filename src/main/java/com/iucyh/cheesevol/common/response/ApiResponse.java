package com.iucyh.cheesevol.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;

/**
 * 성공, 에러 공통 응답 객체
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

    public static ApiResponse<Void> success(String path) {
        return new ApiResponse<>(true, now(), path, null, null);
    }

    public static ApiResponse<Void> error(ErrorInfo error, String path) {
        return new ApiResponse<>(false, now(), path, null, error);
    }

    private static LocalDateTime now() {
        return LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS); // 초 단위까지만 표시
    }

    /**
     * 발생한 예외의 정보
     * @param code 에러 코드
     * @param message 어떤 예외인지 알 수 있는 설명 (보통 {@code ErrorCode}의 message)
     * @param causes 원인이 된 값들(객체 형식), {@code null}이거나 비어있다면 {@code null}로 설정됨
     */
    public record ErrorInfo(String code, String message, Map<String, Object> causes) {

        public ErrorInfo {
            causes = sanitizeCauses(causes);
        }

        public static ErrorInfo of(String code, String message, Map<String, Object> causes) {
            return new ErrorInfo(code, message, causes);
        }

        public static ErrorInfo of(String code, String message) {
            return new ErrorInfo(code, message, null);
        }

        private static Map<String, Object> sanitizeCauses(Map<String, Object> causes) {
            if (causes == null || causes.isEmpty()) {
                return null;
            }
            return Map.copyOf(causes);
        }
    }
}
