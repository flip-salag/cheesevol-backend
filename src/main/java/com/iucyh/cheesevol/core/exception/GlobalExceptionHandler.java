package com.iucyh.cheesevol.core.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iucyh.cheesevol.base.exception.BusinessException;
import com.iucyh.cheesevol.base.exception.ErrorCode;
import com.iucyh.cheesevol.common.response.envelope.FailResponse;
import com.iucyh.cheesevol.common.util.IpUtil;
import com.iucyh.cheesevol.core.exception.errorcode.SystemErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String LOG_FORMAT = "REQUEST: {} {} | IP: {} \n MESSAGE: {} CAUSE: {}";
    private static final String LOG_LEVEL_INFO = "info";
    private static final String LOG_LEVEL_WARN = "warn";
    private static final String LOG_LEVEL_ERROR = "error";

    private final ObjectMapper objectMapper;

    @ExceptionHandler
    public ResponseEntity<FailResponse> handleBusinessException(BusinessException ex, HttpServletRequest req) {
        String path = req.getRequestURI();

        FailResponse failResponse = FailResponse.from(ex, path);
        log(LOG_LEVEL_WARN, req, ex, ex.getCauses());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(failResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.NO_RESOURCE_FOUND;

        FailResponse failResponse = FailResponse.of(errorCode, path);
        log(LOG_LEVEL_INFO, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.MISSING_SERVLET_REQUEST_PARAMETER;

        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();
        LinkedHashMap<String, Object> causes = new LinkedHashMap<>();

        causes.put("parameterName", parameterName);
        causes.put("parameterType", parameterType);

        FailResponse failResponse = FailResponse.of(errorCode, path, causes);
        log(LOG_LEVEL_INFO, req, ex, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @ExceptionHandler
    public ResponseEntity<FailResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH;

        String parameterName = ex.getName();
        String parameterType = ex.getRequiredType() == null ? "" : ex.getRequiredType().getSimpleName();
        LinkedHashMap<String, Object> causes = new LinkedHashMap<>();

        causes.put("parameterName", parameterName);
        causes.put("requiredType", parameterType);

        FailResponse failResponse = FailResponse.of(errorCode, path, causes);
        log(LOG_LEVEL_INFO, req, ex, causes);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.MISSING_PATH_VARIABLE;

        Map<String, Object> causes = null;
        boolean isMissingAfterConversion = ex.isMissingAfterConversion();
        if (isMissingAfterConversion) {
            String variableName = ex.getVariableName();
            causes = Map.of("missingVariable", variableName);
        }

        HttpStatus statusCode = isMissingAfterConversion ? errorCode.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        FailResponse failResponse = FailResponse.of(errorCode, path, causes);
        log(LOG_LEVEL_WARN, req, ex, causes);
        return ResponseEntity
                .status(statusCode)
                .body(failResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.HTTP_MESSAGE_NOT_READABLE;

        FailResponse failResponse = FailResponse.of(errorCode, path);
        log(LOG_LEVEL_WARN, req, ex, null); // TODO: 원인이 된 Request Body 로깅
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpServletRequest req = getRequest(request);
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.VALIDATION_FAILED;

        List<LinkedHashMap<String, String>> failedFields = getFailedFields(ex);
        Map<String, Object> fieldErrors = Map.of("fields", failedFields);

        FailResponse failResponse = FailResponse.of(errorCode, path, fieldErrors);
        log(LOG_LEVEL_INFO, req, ex, fieldErrors);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @ExceptionHandler
    public ResponseEntity<FailResponse> handleDuplicateKeyException(DuplicateKeyException ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.DUPLICATE_KEY;

        FailResponse failResponse = FailResponse.of(errorCode, path);
        log(LOG_LEVEL_WARN, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @ExceptionHandler
    public ResponseEntity<FailResponse> handleOptimisticLockingFailureException(OptimisticLockingFailureException ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.OPTIMISTIC_LOCKING_FAILURE;

        FailResponse failResponse = FailResponse.of(errorCode, path);
        log(LOG_LEVEL_WARN, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    @ExceptionHandler
    public ResponseEntity<FailResponse> handleException(Exception ex, HttpServletRequest req) {
        String path = req.getRequestURI();
        ErrorCode errorCode = SystemErrorCode.INTERNAL_SERVER_ERROR;

        FailResponse failResponse = FailResponse.of(errorCode, path);
        log(LOG_LEVEL_ERROR, req, ex, null);
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(failResponse);
    }

    private HttpServletRequest getRequest(WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        return servletWebRequest.getRequest();
    }

    private List<LinkedHashMap<String, String>> getFailedFields(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return bindingResult.getFieldErrors().stream()
                .map((error) -> {
                    String message = error.getDefaultMessage() == null ? "" : error.getDefaultMessage();

                    LinkedHashMap<String, String> result = new LinkedHashMap<>();
                    result.put("field", error.getField());
                    result.put("message", message);
                    return result;
                })
                .toList();
    }

    private void log(String level, HttpServletRequest request, Exception ex, Map<String, Object> causes) {
        String ip = IpUtil.getIpAddr(request);
        String causeMessage = "{}";
        if (causes != null) {
            try {
                causeMessage = objectMapper.writeValueAsString(causes);
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize causes to json string", e);
            }
        }

        switch (level) {
            case LOG_LEVEL_INFO:
                log.info(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage);
                break;
            case LOG_LEVEL_WARN:
                log.warn(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage);
                break;
            case LOG_LEVEL_ERROR:
                log.error(LOG_FORMAT, request.getMethod(), request.getRequestURI(), ip, ex.getMessage(), causeMessage, ex);
                break;
        }
    }
}

