package com.mg.core.common.handler;

import com.mg.core.common.code.ErrorCode;
import com.mg.core.common.exception.MGException;
import com.mg.core.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${custom.error.includeDevMessage}")
    private boolean includeDevMessage;

    /**
     * Handles SPException and returns an appropriate ErrorResponse.
     *
     * @param e the exception thrown
     * @return ResponseEntity containing the error response and HTTP status
     */
    @ExceptionHandler(MGException.class)
    public ResponseEntity<ErrorResponse> handlerM1n67unException(MGException e) {
        log.error("SPException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = includeDevMessage ? ErrorResponse.of(e.getErrorCode(), e.getMessage())
                : ErrorResponse.of(e.getErrorCode());

        return new ResponseEntity<>(errorResponse, e.getErrorCode().getHttpStatus());
    }

    /**
     * Handles MethodArgumentNotValidException and returns an appropriate
     * ErrorResponse.
     *
     * @param e the exception thrown
     * @return ResponseEntity containing the error response and HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = includeDevMessage ? ErrorResponse.of(ErrorCode.INVALID_PARAMETER, e.getMessage())
                : ErrorResponse.of(ErrorCode.INVALID_PARAMETER);

        return new ResponseEntity<>(errorResponse, ErrorCode.INVALID_PARAMETER.getHttpStatus());
    }

    /**
     * Handles HttpRequestMethodNotSupportedException and returns an appropriate
     * ErrorResponse.
     *
     * @param e the exception thrown
     * @return ResponseEntity containing the error response and HTTP status
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage(), e);

        ErrorResponse errorResponse = includeDevMessage
                ? ErrorResponse.of(ErrorCode.METHOD_NOT_SUPPORTED, e.getMessage())
                : ErrorResponse.of(ErrorCode.METHOD_NOT_SUPPORTED);

        return new ResponseEntity<>(errorResponse, ErrorCode.METHOD_NOT_SUPPORTED.getHttpStatus());
    }

    /**
     * Handles Exception and returns an appropriate ErrorResponse.
     *
     * @param e the exception thrown
     * @return ResponseEntity containing the error response and HTTP status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);

        ErrorResponse errorResponse = includeDevMessage
                ? ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage())
                : ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(errorResponse, ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }

}
