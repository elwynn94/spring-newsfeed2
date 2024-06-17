package com.sparta.springnewsfeed.post;

import com.sparta.springnewsfeed.auth.JwtUtil;
import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private PostController postController;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private User user;

    @Test
    void testCreatePost() {
        // given
        Post post = new Post();
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.CREATED, "Post created successfully");

        when(userDetails.getUser()).thenReturn(user);
        when(postService.createPost(post)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.createPost(userDetails, post);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).createPost(post);
    }

    @Test
    void testGetAllPosts() {
        // given
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "All posts retrieved successfully");

        when(postService.getAllPosts()).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.getAllPosts();

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testGetPostsOfFollowees() {
        // given
        String userId = "1";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token");
        String token = "token";
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "Followees' posts retrieved successfully");

        when(jwtUtil.getAccessTokenFromHeader(request)).thenReturn(token);
        when(postService.getPostsOfFollowees(token, userId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.getPostsOfFollowees(userId, request);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).getPostsOfFollowees(token, userId);
    }

    @Test
    void testGetPostsByUserId() {
        // given
        String userId = "1";
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "User's posts retrieved successfully");

        when(postService.getPostsByUserId(userId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.getPostsByUserId(userId);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).getPostsByUserId(userId);
    }

    @Test
    void testGetPostById() {
        // given
        String userId = "1";
        Long postId = 1L;
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "Post retrieved successfully");

        when(postService.getPostById(userId, postId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.getPostById(userId, postId);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).getPostById(userId, postId);
    }

    @Test
    void testUpdatePost() {
        // given
        Long postId = 1L;
        PostRequest postRequest = new PostRequest();
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "Post updated successfully");

        when(userDetails.getUser()).thenReturn(user);
        when(postService.updatePost(user, postId, postRequest)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.updatePost(postId, postRequest, userDetails);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).updatePost(user, postId, postRequest);
    }

    @Test
    void testDeletePost() {
        // given
        Long postId = 1L;
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "Post deleted successfully");

        when(userDetails.getUser()).thenReturn(user);
        when(postService.deletePost(user, postId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = postController.deletePost(postId, userDetails);

        // then
        assertEquals(responseDto, result);
        verify(postService, times(1)).deletePost(user, postId);
    }
}
