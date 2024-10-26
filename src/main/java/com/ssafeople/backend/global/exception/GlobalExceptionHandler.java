package com.ssafeople.backend.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.response.failure.ErrorResponse;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@NonNullApi
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(SsafeopleException.class)
    public ResponseEntity<ErrorResponse> SsafeopleExceptionHandler(SsafeopleException e,
        HttpServletRequest request) {
        log.error("SsafeopleException {}", e.getMessage());

        ErrorCode code = e.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(code.getStatus(), code.getReason(),
            request.getRequestURL().toString()
        );

        log.info(errorResponse.getPath());

        return ResponseEntity.status(HttpStatus.valueOf(code.getStatus())).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
        ConstraintViolationException e, HttpServletRequest request) {
        log.error("Constraint violation error {}", e.getMessage());

        String errorMessages = e.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));

        String url = request.getRequestURL().toString();

        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            errorMessages,
            url
        );

        log.info(errorResponse.getPath());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e,
        HttpServletRequest request) {
        log.error("INTERNAL_SERVER_ERROR", e);

        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        String url = requestURL.toString();

        ErrorCode internalServerError = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(internalServerError.getStatus(),
            internalServerError.getReason(), url
        );

        return ResponseEntity.status(HttpStatus.valueOf(internalServerError.getStatus()))
            .body(errorResponse);
    }

    @SneakyThrows
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {

        log.error("MethodArgumentNotValidException {}", ex.getMessage());

        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String url = servletWebRequest.getRequest().getRequestURL().toString();

        Map<String, String> fieldAndErrorMessages = errors.stream()
            .collect(
                Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage()
                        : "입력값에 오류가 있습니다."
                )
            );
        String errorToJsonString = objectMapper.writeValueAsString(fieldAndErrorMessages);
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errorToJsonString, url);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {

        log.error("HttpRequestMethodNotSupportedException {}", ex.getMessage());

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String url = servletWebRequest.getRequest().getRequestURL().toString();
        ErrorResponse errorResponse = new ErrorResponse(status.value(),
            ErrorCode.METHOD_NOT_ALLOWED.getReason(), url);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
        HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("NoResourceFoundException {}", ex.getMessage());

        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        String url = servletWebRequest.getRequest().getRequestURL().toString();
        ErrorResponse errorResponse = new ErrorResponse(status.value(),
            ErrorCode.URL_INPUT_ERROR.getReason(), url);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}