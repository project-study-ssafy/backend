package com.ssafeople.backend.domain.admin.presentation;

import com.ssafeople.backend.domain.admin.service.AdminService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.admin.AdminLoginFailedException;
import com.ssafeople.backend.global.exception.admin.NotAdminException;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("redirectUrl") String redirectUrl) {
        log.info("redirectUrl: {}", redirectUrl);
        return "admin/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
        HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User admin = adminService.login(email, password);
            session.setAttribute("admin", admin);
        } catch (UserNotFoundException | NotAdminException | AdminLoginFailedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            log.info(e.getMessage());
            return "redirect:/admin/login";
        }

        String redirectUri = (String) session.getAttribute("redirectUri");
        log.info("Redirecting to: {}", redirectUri);

        if (redirectUri != null && !redirectUri.isEmpty()) {
            log.info("Redirecting to: {}", redirectUri);
            return "redirect:" + redirectUri;
        }

        return "redirect:/admin/home";
    }

}
