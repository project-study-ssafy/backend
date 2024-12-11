package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostUpdateRequest;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import com.ssafeople.backend.global.exception.user.PostOwnerIsNotCurrentUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

        //When
        List<PostSummaryResponse> posts = postService.getPostsByBoardId(testBoard.getId());

        //Then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getId()).isEqualTo(post.getId());

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

    @Test
    @DisplayName("게시글 상세 조회 성공")
    void getPostById_success() {
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);

        Post post = new Post("테스트 게시물 제목", "테스트 게시물 내용", testUser, testBoard);
        postRepository.save(post);

        PostDetailResponse response = postService.getPostById(post.getId());
        assertThat(response.getId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("게시글 상세 조회 실패 (존재하지 않는 게시글 접근) 예외를 발생시킨다.")
    void getPostById_fail() {
        assertThrows(PostNotExistException.class, () -> postService.getPostById(999L));
    }

    @Test
    @DisplayName("게시글 작성 성공")
    void writePost_success() {
        //Given
        Board testBoard = new Board("TEST", "TEST BOARD");
        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        PostWriteRequest postWriteRequest = new PostWriteRequest();

        postWriteRequest.setTitle("Test post title");
        postWriteRequest.setContent("Test post content");

        Post post = new Post(postWriteRequest.getTitle(), postWriteRequest.getContent(), testUser, testBoard);
        //When & Then
        assertThat(postRepository.save(post) == post).isTrue();
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_success() {

        //given
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);

        Post post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest();
        postUpdateRequest.setTitle("Update title");
        postUpdateRequest.setContent("Update content");

        //when
        postService.updatePost(postUpdateRequest, post.getId(), testUser);

        //then
        assertEquals("Update title", post.getTitle());
        assertEquals("Update content", post.getContent());
    }

    @Test
    @DisplayName("게시글 수정 실패: 현재 사용자와 게시글 소유주가 다르면 예외를 반환한다")
    void updatePost_fail() {
        //given
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        User testUser1 = new User("testUserName1", "test123@test.test1", "", "TestUserNickName1");
        userRepository.save(testUser);
        userRepository.save(testUser1);


        Post post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);

        PostUpdateRequest postUpdateRequest = new PostUpdateRequest();
        postUpdateRequest.setTitle("Update title");
        postUpdateRequest.setContent("Update content");

        //when & then
        assertThrows(PostOwnerIsNotCurrentUserException.class, () ->
                postService.updatePost(postUpdateRequest, post.getId(), testUser1)
        );
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deletePost_success() {
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);

        Post post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);

        postService.deletePost(post.getId(), testUser);

        assertFalse(postRepository.findById(post.getId()).isPresent());
    }

    @Test
    @DisplayName("게시글 삭제 실패 (권한 없음) 예외 반환")
    void deletePost_fail() {
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);

        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        User testUser1 = new User("testUserName1", "test123@test.test1", "", "TestUser1NickName");
        userRepository.save(testUser);
        userRepository.save(testUser1);

        Post post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);

        assertThrows(PostOwnerIsNotCurrentUserException.class, () -> postService.deletePost(post.getId(), testUser1));
    }

    @Test
    @DisplayName("게시글 삭제 실패 (존재하지 않는 게시글) 예외 반환")
    void deletePost_fail2() {
        Board testBoard = new Board("TEST", "TEST BOARD");
        boardRepository.save(testBoard);
        User testUser = new User("testUserName", "test123@test.test", "", "TestUserNickName");
        userRepository.save(testUser);
        Post post = new Post("TT", "TC", testUser, testBoard);
        postRepository.save(post);

        assertThrows(PostNotExistException.class, () -> postService.deletePost(post.getId()+1, testUser));
    }
}
