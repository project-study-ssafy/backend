package com.ssafeople.backend.domain.admin.presentation;

import com.ssafeople.backend.domain.admin.service.AdminService;
import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    private final PostRepository postRepository;

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

    @AdminOnly
    @GetMapping("/users/{userId}")
    public String user(@PathVariable Short userId, Model model) {
        User userById = userService.getUserById(userId);
        model.addAttribute("user", userById);
        return "admin/users/user";
    }

    @AdminOnly
    @PostMapping("/users/{userId}/delete")
    public String delete(@PathVariable Short userId) {
        User user = userService.getUserById(userId);
        userService.withdraw(user);
        return "redirect:/admin/users";
    }

    @AdminOnly
    @GetMapping("/boards")
    public String boards(Model model) {
        List<Board> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "admin/boards/boardForm";
    }

    @AdminOnly
    @PostMapping("/boards/create")
    public String createBoard(@RequestParam String boardName, @RequestParam String description) {
        boardService.writeBoard(boardName, description);
        return "redirect:/admin/boards";
    }

    @AdminOnly
    @PostMapping("/boards/{boardId}/delete")
    public String deleteBoard(@PathVariable Short boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/admin/boards";
    }

    @AdminOnly
    @GetMapping("/boards/{boardId}")
    public String board(@PathVariable Short boardId,
        @RequestParam(defaultValue = "1") int page,
        Model model) {
        Page<PostSummaryResponse> pagedPosts = postService.getPagedPostsByBoardId(boardId, page,
            20);
        model.addAttribute("posts", pagedPosts);
        model.addAttribute("boardId", boardId);
        return "admin/posts/posts";
    }

    @AdminOnly
    @GetMapping("/posts/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        PostDetailResponse post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "admin/posts/post";
    }

    @AdminOnly
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return "redirect:/admin/home";
    }
}
