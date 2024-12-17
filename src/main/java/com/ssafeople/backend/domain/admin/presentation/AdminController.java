package com.ssafeople.backend.domain.admin.presentation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("redirectUrl") String redirectUrl) {
        log.info("redirectUrl: {}", redirectUrl);
        return "admin/loginForm";
    }

}
