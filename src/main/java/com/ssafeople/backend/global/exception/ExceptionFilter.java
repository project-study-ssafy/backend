package com.ssafeople.backend.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.response.failure.ErrorResponse;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@NonNullApi
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (SsafeopleException e) {
            writeErrorResponse(request, response, e.getErrorCode());
        } catch (Exception e) {
            if (e.getCause() instanceof SsafeopleException) {
                writeErrorResponse(request, response, ((SsafeopleException) e.getCause()).getErrorCode());
            } else {
                log.error(e.getMessage(), e);
                writeErrorResponse(request, response, ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void writeErrorResponse(HttpServletRequest request, HttpServletResponse response,
        ErrorCode errorCode) throws IOException {

        ErrorResponse errorResponse = new ErrorResponse(errorCode.getStatus(),
            errorCode.getReason(), request.getRequestURL().toString());

        response.setStatus(errorCode.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}