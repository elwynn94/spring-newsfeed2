package com.sparta.springnewsfeed.post;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post post;
    private User user;
    private List<Post> posts;
    private PostRequest request;

    @BeforeEach
    void setUp() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .build();
        request = fixtureMonkey.giveMeOne(PostRequest.class);
        post = fixtureMonkey.giveMeOne(Post.class);
        user = fixtureMonkey.giveMeOne(User.class);
        posts = fixtureMonkey.giveMe(Post.class, 5);
        post.setUser(user);
    }

    @Test
    @DisplayName("게시글 생성: 성공")
    void createPost_Success() {
        // Given
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        HttpStatusResponseDto response = postService.createPost(post);

        // Then
        verify(postRepository).save(post);
        assertEquals(ResponseCode.CREATED.getStatusCode(), response.getStatusCode());
    }

    @Test
    @DisplayName("게시글 목록 조회: 성공 (게시글 존재)")
    void getAllPosts_Success_WithPosts() {
        // Given
        when(postRepository.findAll()).thenReturn(posts);

        // When
        HttpStatusResponseDto response = postService.getAllPosts();

        // Then
        verify(postRepository).findAll();
        List<PostResponse> postResponses = (List<PostResponse>) response.getData();
        assertEquals(posts.size(), postResponses.size());
    }

    @Test
    @DisplayName("게시글 목록 조회: 성공 (게시글 없음)")
    void getAllPosts_Success_NoPosts() {
        // Given
        when(postRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        HttpStatusResponseDto response = postService.getAllPosts();

        // Then
        verify(postRepository).findAll();
        assertEquals(ResponseCode.SUCCESS.getStatusCode(), response.getStatusCode());
        assertEquals("먼저 작성하여 소식을 알려보세요", response.getData());
    }

    @Test
    @DisplayName("게시글 수정: 성공")
    void updatePost_Success() {
        // Given
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        HttpStatusResponseDto response = postService.updatePost(user, post.getId(), request);

        // Then
        assertEquals(ResponseCode.SUCCESS.getStatusCode(), response.getStatusCode());
        assertEquals(post.getId(), ((PostResponse) response.getData()).getPostId());
    }

    @Test
    @DisplayName("게시글 수정: 실패 (게시글 없음)")
    void updatePost_Failure_PostNotFound() {
        // Given
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        // When
        HttpStatusResponseDto response = postService.updatePost(user, post.getId(), request);

        // Then
        assertEquals(ResponseCode.ENTITY_NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    @Test
    @DisplayName("게시글 삭제: 성공")
    void deletePost_Success() {
        // Given
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When
        HttpStatusResponseDto response = postService.deletePost(user, post.getId());

        // Then
        assertEquals(ResponseCode.SUCCESS.getStatusCode(), response.getStatusCode());
        verify(postRepository).delete(post);
    }

}