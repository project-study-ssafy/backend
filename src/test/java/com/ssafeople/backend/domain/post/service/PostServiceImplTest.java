package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PostServiceImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("게시글 목록 반환 성공")
    void getPostsByBoardId_success() {
        //Given
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);
        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);
        Post post = new Post("테스트 게시물 제목", "테스트 게시물 내용", testUser, testBoard);
        postRepository.save(post);
        PostSummaryResponse response =
                PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .userName(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt())
                        .build();

        //When
        List<PostSummaryResponse> posts = postService.getPostsByBoardId(testBoard.getId());

        //Then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(response.getId());

    }

    @Test
    @DisplayName("게시글 목록을 반환하는데 실패하면 예외를 발생시킨다.")
    void getPostsByBoardId_fail() {
        //Given
        Board testBoard = new Board("TEST", "TEST BOARD");
        Board testBoard1 = new Board("TEST1", "TEST BOARD 1");
        boardRepository.save(testBoard);
        boardRepository.save(testBoard1);
        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);
        Post post = new Post("테스트 게시물 제목", "테스트 게시물 내용", testUser, testBoard1);
        postRepository.save(post);
        //When & Then
        assertThrows(PostListEmptyException.class, () -> postService.getPostsByBoardId(testBoard.getId()));
    }
}
