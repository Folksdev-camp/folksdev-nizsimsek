package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.TestSupport;
import com.nizsimsek.blogApp.dto.CommentDto;
import com.nizsimsek.blogApp.dto.converter.CommentDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateCommentReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.Post;
import com.nizsimsek.blogApp.model.Comment;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.CommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class CommentServiceTest extends TestSupport {
    
    private CommentRepository commentRepository;
    private CommentDtoConverter commentDtoConverter;
    private UserService userService;
    private PostService postService;
    
    private CommentService commentService;
    
    @BeforeEach
    void setUp() {
        
        commentRepository = mock(CommentRepository.class);
        commentDtoConverter = mock(CommentDtoConverter.class);
        userService = mock(UserService.class);
        postService = mock(PostService.class);
        
        commentService = new CommentService(commentRepository, commentDtoConverter, userService, postService);
    }

    @Test
    void testCreateComment_whenCreateCommentReqValid_shouldReturnCommentDto() {

        CreateCommentReq createCommentReq = generateCreateCommentReq();
        Comment comment = generateComment();
        CommentDto commentDto = generateCommentDto();
        User user = generateUser();
        Post post = generatePost();

        when(userService.findUserById(createCommentReq.getAuthorId())).thenReturn(user);
        when(postService.findPostById(createCommentReq.getPostId())).thenReturn(post);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.createComment(createCommentReq);

        assertEquals(commentDto, result);

        verify(userService).findUserById("id");
        verify(postService).findPostById("id");
        verify(commentRepository).save(comment);
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testGetAllComments_shouldReturnCommentDtos() {

        List<Comment> comments = generateComments();
        List<CommentDto> commentDtos = generateCommentDtos();

        when(commentRepository.findAll()).thenReturn(comments);
        when(commentDtoConverter.convertToCommentDtos(comments)).thenReturn(commentDtos);

        List<CommentDto> result = commentService.getAllComments();

        assertEquals(commentDtos, result);

        verify(commentRepository).findAll();
        verify(commentDtoConverter).convertToCommentDtos(comments);
    }

    @Test
    void testGetCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.getCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testGetCommentById_whenIdExist_shouldReturnCommentDto() {

        Comment comment = generateComment();
        CommentDto commentDto = generateCommentDto();

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(comment));
        assert comment != null;
        when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.getCommentById("id");

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testUpdateComment_whenUpdateCommentReqInValid_shouldThrowGeneralNotFoundException() {

        UpdateCommentReq updateCommentReq = generateUpdateCommentReq();

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.updateComment("id", updateCommentReq));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testUpdateComment_whenUpdateCommentReqValid_shouldReturnCommentDto() {
        UpdateCommentReq updateCommentReq = generateUpdateCommentReq();
        Comment comment = generateComment();
        CommentDto commentDto = generateCommentDto();

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(comment));
        assert comment != null;
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentDtoConverter.convert(comment)).thenReturn(commentDto);

        CommentDto result = commentService.updateComment("id", updateCommentReq);

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentRepository).save(comment);
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testDeleteCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.getCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testDeleteCommentById_whenIdExist() {

        commentService.deleteCommentById("id");

        verify(commentRepository).deleteById("id");
    }

    @Test
    void testLikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.findCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testLikeCommentById_whenIdExist_shouldReturnCommentDto() {

        Comment comment = generateCommentAllFields();
        CommentDto commentDto = generateCommentDto();
        long likeCount = comment.getLikes();

        assert likeCount >= 0;
        likeCount += 1;

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                likeCount,
                comment.getDislikes(),
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost()
        );

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedComment));
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentDtoConverter.convert(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.likeCommentById("id");

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentRepository).save(updatedComment);
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testUnlikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.findCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testUnlikeCommentById_whenIdExist_shouldReturnCommentDto() {

        Comment comment = generateCommentAllFields();
        CommentDto commentDto = generateCommentDto();
        long likeCount = comment.getLikes();

        assert likeCount > 0;
        likeCount -= 1;

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                likeCount,
                comment.getDislikes(),
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost()
        );

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedComment));
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentDtoConverter.convert(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.unlikeCommentById("id");

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentRepository).save(updatedComment);
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testDislikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.findCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testDislikeCommentById_whenIdExist_shouldReturnCommentDto() {

        Comment comment = generateCommentAllFields();
        CommentDto commentDto = generateCommentDto();
        long dislikeCount = comment.getDislikes();

        assert dislikeCount >= 0;
        dislikeCount += 1;

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                dislikeCount,
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost()
        );

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedComment));
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentDtoConverter.convert(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.dislikeCommentById("id");

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentRepository).save(updatedComment);
        verify(commentDtoConverter).convert(comment);
    }

    @Test
    void testUndislikeCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(commentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> commentService.findCommentById("id"));

        verify(commentRepository).findById("id");
        verifyNoInteractions(commentDtoConverter);
    }

    @Test
    void testUndislikeCommentById_whenIdExist_shouldReturnCommentDto() {

        Comment comment = generateCommentAllFields();
        CommentDto commentDto = generateCommentDto();
        long dislikeCount = comment.getDislikes();

        assert dislikeCount > 0;
        dislikeCount -= 1;

        Comment updatedComment = new Comment(
                comment.getId(),
                comment.getContent(),
                comment.getLikes(),
                dislikeCount,
                comment.getCreatedDate(),
                comment.getUpdatedDate(),
                comment.getAuthor(),
                comment.getPost()
        );

        when(commentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedComment));
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
        when(commentDtoConverter.convert(updatedComment)).thenReturn(commentDto);

        CommentDto result = commentService.undislikeCommentById("id");

        assertEquals(commentDto, result);

        verify(commentRepository).findById("id");
        verify(commentRepository).save(updatedComment);
        verify(commentDtoConverter).convert(comment);
    }


    @AfterEach
    void tearDown() {
    }
}