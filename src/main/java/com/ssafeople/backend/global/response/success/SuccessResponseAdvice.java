package com.ssafeople.backend.global.response.success;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@NonNullApi
@RestControllerAdvice(basePackages = "com.ssafeople.backend")
public class SuccessResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
        MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse)response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus httpStatus = HttpStatus.resolve(status);

        // HTTP 상태가 2xx가 아닌 경우 원래의 본문 반환
        if (httpStatus == null || !httpStatus.is2xxSuccessful()) {
            return body;
        }

        log.info("SuccessResponse 반환 {}", body);

        return new SuccessResponse(status, body);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }
}