package com.iucyh.flip.core.response;

import com.iucyh.flip.common.response.envelope.FailResponse;
import com.iucyh.flip.common.response.envelope.SuccessResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 컨트롤러가 반환한 도메인 응답 DTO를 {@link SuccessResponse}로 자동 래핑
 * <p><b>제외</b>: 이미 {@link SuccessResponse}로 래핑된 응답, {@link FailResponse} (전역 예외 핸들러를 통해 래핑됨), 기타 JSON 본문이 아닌 것들 ({@code CharSequence}, {@code byte[]}, {@code Resource})</p>
 */
@RestControllerAdvice(basePackages = "com.iucyh.flip") // 직접 개발한 API에 대해서만 적용 (프레임워크나 라이브러리가 제공하는 API는 제외)
public class SuccessResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> parameterType = returnType.getParameterType();
        if (SuccessResponse.class.isAssignableFrom(parameterType)
                || FailResponse.class.isAssignableFrom(parameterType)
                || CharSequence.class.isAssignableFrom(parameterType)
                || byte[].class.isAssignableFrom(parameterType)
                || Resource.class.isAssignableFrom(parameterType)
        ) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return SuccessResponse.empty();
        }

        // 혹시 모를 버그 방지 & ResponseEntity 타입 대응
        if (body instanceof SuccessResponse<?>
                || body instanceof FailResponse
                || body instanceof CharSequence
                || body instanceof byte[]
                || body instanceof Resource
        ) {
            return body;
        }

        return SuccessResponse.of(body);
    }
}
