package com.sparta.springnewsfeed.comment;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.post.Post;
import com.sparta.springnewsfeed.post.PostRepository;
import com.sparta.springnewsfeed.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private CommentRequestDto requestDto;

    @BeforeEach
    void setUp() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .build();
        user = fixtureMonkey.giveMeBuilder(User.class).sample();
        post = fixtureMonkey.giveMeBuilder(Post.class).sample();
        requestDto = fixtureMonkey.giveMeBuilder(CommentRequestDto.class).sample();
    }

    @Test
    @DisplayName("댓글 추가: 성공")
    void addComment_Success() {
        // Given
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // When
        HttpStatusResponseDto response = commentService.addComment(post.getId(), requestDto, user);

        // Then
        assertEquals(ResponseCode.CREATED.getStatusCode(), response.getStatusCode());
        verify(commentRepository).save(org.mockito.ArgumentMatchers.any(Comment.class));
    }

    @Test
    @DisplayName("댓글 추가: 실패 (잘못된 postId)")
    void addComment_Failure_InvalidPostId() {
        // Given
        when(postRepository.findById(post.getId())).thenReturn(Optional.empty());

        // When
        HttpStatusResponseDto response = commentService.addComment(post.getId(), requestDto, user);

        // Then

        assertEquals(ResponseCode.INVALID_INPUT_VALUE.getStatusCode(), response.getStatusCode());
        assertEquals("유효하지 않은 입력 값입니다.", response.getMessage());
    }


}