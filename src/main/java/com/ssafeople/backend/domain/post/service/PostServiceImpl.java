package com.ssafeople.backend.domain.post.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.post.domain.Post;
import com.ssafeople.backend.domain.post.domain.repository.PostRepository;

import com.ssafeople.backend.domain.post.presentation.dto.request.PostUpdateRequest;
import com.ssafeople.backend.domain.post.presentation.dto.request.PostWriteRequest;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostDetailResponse;
import com.ssafeople.backend.domain.post.presentation.dto.response.PostSummaryResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.post.PostListEmptyException;
import com.ssafeople.backend.global.exception.post.PostNotExistException;
import com.ssafeople.backend.global.exception.user.PostOwnerIsNotCurrentUserException;
import com.ssafeople.backend.global.utils.upload.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final BoardService boardService;

    private final ImageUtils imageUtils;

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getPostsRegardlessBoardId() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        if (posts.isEmpty()) {
            throw PostListEmptyException.EXCEPTION;
        }

        List<PostSummaryResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            PostSummaryResponse response =
                    PostSummaryResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickName(post.getUser().getNickname())
                            .createdAt(post.getCreatedAt())
                            .commentCount((short) post.getComments().size())
                            .likeCount((short) post.getLikes().size())
                            .viewCount(post.getViewCount())
                            .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getPostsByBoardId(Short boardId) {
        List<Post> posts = postRepository.findByBoardId(boardId);

        if (posts.isEmpty()) {
            throw PostListEmptyException.EXCEPTION;
        }

        List<PostSummaryResponse> responses = new ArrayList<>();
        for (Post post : posts) {
            PostSummaryResponse response =
                    PostSummaryResponse.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .nickName(post.getUser().getNickname())
                            .createdAt(post.getCreatedAt())
                            .commentCount((short) post.getComments().size())
                            .likeCount((short) post.getLikes().size())
                            .viewCount(post.getViewCount())
                            .imageUrl(post.getImageUrls().isEmpty() ? null : post.getImageUrls().get(0))
                            .build();
            responses.add(response);
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPagedPostsByBoardId(Short boardId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size).withSort(Sort.by(Sort.Direction.DESC, "id"));

        Page<Post> postPage = postRepository.findByBoardId(boardId, pageable);

        if (postPage.isEmpty()) {
            throw PostListEmptyException.EXCEPTION;
        }

        List<PostSummaryResponse> responses = postPage.getContent().stream()
                .map(post -> PostSummaryResponse.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .nickName(post.getUser().getNickname())
                        .createdAt(post.getCreatedAt())
                        .commentCount((short) post.getComments().size())
                        .likeCount((short) post.getLikes().size())
                        .viewCount(post.getViewCount())
                        .imageUrl(post.getImageUrls().isEmpty() ? null : post.getImageUrls().get(0))
                        .build())
                .toList();

        return new PageImpl<>(responses, pageable, postPage.getTotalElements());
    }

    @Override
    public PostDetailResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        post.view();

        return PostDetailResponse
                .builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .nickName(post.getUser().getNickname())
                .imageUrls(post.getImageUrls())
                .commentCount((short) post.getComments().size())
                .viewCount(post.getViewCount())
                .likeCount((short) post.getLikes().size())
                .build();
    }

    @Override
    public void writePost(PostWriteRequest request, Short boardId, User user) {

        Board board = boardService.getBoardById(boardId);

        List<String> imageUrls = uploadFiles(request.getImages());

        Post post = new Post(request.getTitle(), request.getContent(), user, board, imageUrls);

        postRepository.save(post);
    }

    //Image Files 있을 경우 Upload 위해서 경로 짜는 함수. PostService 내부에서만 사용할 것임
    private List<String> uploadFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        return files.stream()
                .map(file -> imageUtils.uploadImage(file, "imgs/post"))
                .collect(Collectors.toList());
    }

    @Override
    public void updatePost(PostUpdateRequest request, Long postId, User user) {

        Post post = postRepository.findById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        if (!(post.getUser().equals(user))) {
           throw PostOwnerIsNotCurrentUserException.EXCEPTION;
        }

        List<String> oldImageUrls = post.getImageUrls();
        deleteFiles(oldImageUrls);

        List<String> newImageUrls = uploadFiles(request.getImages());

        post.update(request.getTitle(), request.getContent(), newImageUrls);
    }

    @Override
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> PostNotExistException.EXCEPTION);

        if (!(post.getUser().equals(user))) {
            throw PostOwnerIsNotCurrentUserException.EXCEPTION;
        }

        deleteFiles(post.getImageUrls());

        postRepository.delete(post);
    }

    private void deleteFiles(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        imageUtils.deleteImages(imageUrls);
    }

    @Override
    public Long getCount() {
        return postRepository.count();
    }

}
