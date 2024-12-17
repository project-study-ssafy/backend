package com.ssafeople.backend.domain.admin.presentation;

import com.ssafeople.backend.domain.admin.service.AdminService;
import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.post.service.PostService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.service.UserService;
import com.ssafeople.backend.global.annotation.AdminOnly;
import com.ssafeople.backend.global.exception.admin.AdminLoginFailedException;
import com.ssafeople.backend.global.exception.admin.NotAdminException;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final BoardService boardService;
    private final UserService userService;
    private final PostService postService;

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

    @AdminOnly
    @GetMapping("/home")
    public String adminHome(Model model) {

        Long postCount = postService.getCount();
        Long userCount = userService.getCount();

        model.addAttribute("postCount", postCount);
        model.addAttribute("userCount", userCount);

        List<Board> boards = boardService.getAllBoards();

        model.addAttribute("boards", boards);

        return "admin/home";
    }

    @AdminOnly
    @GetMapping("/users")
    public String users(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<User> userPage = userService.findAll(pageable);

        model.addAttribute("userPage", userPage); // 사용자 페이지 객체를 모델에 추가
        return "admin/users/users"; // Thymeleaf 템플릿 이름
    }


}
