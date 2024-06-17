package com.sparta.springnewsfeed.comment;

import com.sparta.springnewsfeed.auth.UserDetailsImpl;
import com.sparta.springnewsfeed.common.HttpStatusResponseDto;
import com.sparta.springnewsfeed.common.ResponseCode;
import com.sparta.springnewsfeed.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private User user;

    @Test
    void testAddComment() {
        // given
        Long postId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.CREATED, "Comment added successfully");
        when(userDetails.getUser()).thenReturn(user);
        when(commentService.addComment(postId, requestDto, user)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = commentController.addComment(userDetails, postId, requestDto);

        // then
        assertEquals(responseDto, result);
        verify(commentService, times(1)).addComment(postId, requestDto, user);
    }

    @Test
    void testGetCommentsByCommentId() {
        // given
        Long postId = 1L;
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.CREATED, "Comments retrieved successfully");

        when(commentService.getComments(postId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = commentController.getCommentsByCommentId(postId);

        // then
        assertEquals(responseDto, result);
        verify(commentService, times(1)).getComments(postId);
    }

    @Test
    void testUpdateComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.CREATED, "Comment updated successfully");

        when(userDetails.getUser()).thenReturn(user);
        when(commentService.updateComment(user, commentId, requestDto)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = commentController.updateComment(postId, commentId, requestDto, userDetails);

        // then
        assertEquals(responseDto, result);
        verify(commentService, times(1)).updateComment(user, commentId, requestDto);
    }

    @Test
    void testDeleteComment() {
        // given
        Long postId = 1L;
        Long commentId = 1L;
        HttpStatusResponseDto responseDto = new HttpStatusResponseDto(ResponseCode.SUCCESS, "Comment deleted successfully");

        when(userDetails.getUser()).thenReturn(user);
        when(commentService.deleteComment(user, commentId)).thenReturn(responseDto);

        // when
        HttpStatusResponseDto result = commentController.deleteComment(commentId, postId, userDetails);

        // then
        assertEquals(responseDto, result);
        verify(commentService, times(1)).deleteComment(user, commentId);
    }
}