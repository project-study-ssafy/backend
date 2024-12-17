package com.ssafeople.backend.global.exception;

import com.ssafeople.backend.global.exception.admin.NotAdminException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(NotAdminException.class)
    public String handleNotAdminException(NotAdminException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error("NotAdminException: {}", e.getMessage());

        String originalUrl = request.getRequestURL().toString();
        request.getSession().setAttribute("redirectUri", originalUrl);

        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/admin/login";
    }
}