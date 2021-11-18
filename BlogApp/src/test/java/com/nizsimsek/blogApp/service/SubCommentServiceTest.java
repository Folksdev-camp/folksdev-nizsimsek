package com.nizsimsek.blogApp.service;

import com.nizsimsek.blogApp.TestSupport;
import com.nizsimsek.blogApp.dto.SubCommentDto;
import com.nizsimsek.blogApp.dto.converter.SubCommentDtoConverter;
import com.nizsimsek.blogApp.dto.request.CreateSubCommentReq;
import com.nizsimsek.blogApp.dto.request.UpdateSubCommentReq;
import com.nizsimsek.blogApp.exception.GeneralNotFoundException;
import com.nizsimsek.blogApp.model.SubComment;
import com.nizsimsek.blogApp.model.Comment;
import com.nizsimsek.blogApp.model.User;
import com.nizsimsek.blogApp.repository.SubCommentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class SubCommentServiceTest extends TestSupport {

    private SubCommentRepository subCommentRepository;
    private SubCommentDtoConverter subCommentDtoConverter;
    private UserService userService;
    private CommentService commentService;

    private SubCommentService subCommentService;

    @BeforeEach
    void setUp() {

        subCommentRepository = mock(SubCommentRepository.class);
        subCommentDtoConverter = mock(SubCommentDtoConverter.class);
        userService = mock(UserService.class);
        commentService = mock(CommentService.class);

        subCommentService = new SubCommentService(subCommentRepository, subCommentDtoConverter, userService, commentService);
    }

    @Test
    void testCreateSubComment_whenCreateSubCommentReqValid_shouldReturnSubCommentDto() {

        CreateSubCommentReq createSubCommentReq = generateCreateSubCommentReq();
        SubComment subComment = generateSubComment();
        SubCommentDto subCommentDto = generateSubCommentDto();
        User user = generateUser();
        Comment comment = generateComment();

        when(userService.findUserById(createSubCommentReq.getAuthorId())).thenReturn(user);
        when(commentService.findCommentById(createSubCommentReq.getCommentId())).thenReturn(comment);
        when(subCommentRepository.save(subComment)).thenReturn(subComment);
        when(subCommentDtoConverter.convert(subComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.createSubComment(createSubCommentReq);

        assertEquals(subCommentDto, result);

        verify(userService).findUserById("id");
        verify(commentService).findCommentById("id");
        verify(subCommentRepository).save(subComment);
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testGetAllSubComments_shouldReturnSubCommentDtos() {

        List<SubComment> subComments = generateSubComments();
        List<SubCommentDto> subCommentDtos = generateSubCommentDtos();

        when(subCommentRepository.findAll()).thenReturn(subComments);
        when(subCommentDtoConverter.convertToSubCommentDtos(subComments)).thenReturn(subCommentDtos);

        List<SubCommentDto> result = subCommentService.getAllSubComments();

        assertEquals(subCommentDtos, result);

        verify(subCommentRepository).findAll();
        verify(subCommentDtoConverter).convertToSubCommentDtos(subComments);
    }

    @Test
    void testGetSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.getSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testGetSubCommentById_whenIdExist_shouldReturnSubCommentDto() {

        SubComment subComment = generateSubComment();
        SubCommentDto subCommentDto = generateSubCommentDto();

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(subComment));
        assert subComment != null;
        when(subCommentDtoConverter.convert(subComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.getSubCommentById("id");

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testUpdateSubComment_whenUpdateSubCommentReqInValid_shouldThrowGeneralNotFoundException() {

        UpdateSubCommentReq updateSubCommentReq = generateUpdateSubCommentReq();

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.updateSubComment("id", updateSubCommentReq));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testUpdateSubComment_whenUpdateSubCommentReqValid_shouldReturnSubCommentDto() {
        UpdateSubCommentReq updateSubCommentReq = generateUpdateSubCommentReq();
        SubComment subComment = generateSubComment();
        SubCommentDto subCommentDto = generateSubCommentDto();

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.ofNullable(subComment));
        assert subComment != null;
        when(subCommentRepository.save(subComment)).thenReturn(subComment);
        when(subCommentDtoConverter.convert(subComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.updateSubComment("id", updateSubCommentReq);

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentRepository).save(subComment);
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testDeleteSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.getSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testDeleteSubCommentById_whenIdExist() {

        subCommentService.deleteSubCommentById("id");

        verify(subCommentRepository).deleteById("id");
    }

    @Test
    void testLikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.findSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testLikeSubCommentById_whenIdExist_shouldReturnSubCommentDto() {

        SubComment subComment = generateSubCommentAllFields();
        SubCommentDto subCommentDto = generateSubCommentDto();
        long likeCount = subComment.getLikes();

        assert likeCount >= 0;
        likeCount += 1;

        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                likeCount,
                subComment.getDislikes(),
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                subComment.getAuthor(),
                subComment.getComment()
        );

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedSubComment));
        when(subCommentRepository.save(updatedSubComment)).thenReturn(updatedSubComment);
        when(subCommentDtoConverter.convert(updatedSubComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.likeSubCommentById("id");

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentRepository).save(updatedSubComment);
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testUnlikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.findSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testUnlikeSubCommentById_whenIdExist_shouldReturnSubCommentDto() {

        SubComment subComment = generateSubCommentAllFields();
        SubCommentDto subCommentDto = generateSubCommentDto();
        long likeCount = subComment.getLikes();

        assert likeCount > 0;
        likeCount -= 1;

        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                likeCount,
                subComment.getDislikes(),
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                subComment.getAuthor(),
                subComment.getComment()
        );

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedSubComment));
        when(subCommentRepository.save(updatedSubComment)).thenReturn(updatedSubComment);
        when(subCommentDtoConverter.convert(updatedSubComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.unlikeSubCommentById("id");

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentRepository).save(updatedSubComment);
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testDislikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.findSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testDislikeSubCommentById_whenIdExist_shouldReturnSubCommentDto() {

        SubComment subComment = generateSubCommentAllFields();
        SubCommentDto subCommentDto = generateSubCommentDto();
        long dislikeCount = subComment.getDislikes();

        assert dislikeCount >= 0;
        dislikeCount += 1;

        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                subComment.getLikes(),
                dislikeCount,
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                subComment.getAuthor(),
                subComment.getComment()
        );

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedSubComment));
        when(subCommentRepository.save(updatedSubComment)).thenReturn(updatedSubComment);
        when(subCommentDtoConverter.convert(updatedSubComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.dislikeSubCommentById("id");

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentRepository).save(updatedSubComment);
        verify(subCommentDtoConverter).convert(subComment);
    }

    @Test
    void testUndislikeSubCommentById_whenIdNotExist_shouldThrowGeneralNotFoundException() {

        when(subCommentRepository.findById("id")).thenThrow(GeneralNotFoundException.class);

        assertThrows(GeneralNotFoundException.class, () -> subCommentService.findSubCommentById("id"));

        verify(subCommentRepository).findById("id");
        verifyNoInteractions(subCommentDtoConverter);
    }

    @Test
    void testUndislikeSubCommentById_whenIdExist_shouldReturnSubCommentDto() {

        SubComment subComment = generateSubCommentAllFields();
        SubCommentDto subCommentDto = generateSubCommentDto();
        long dislikeCount = subComment.getDislikes();

        assert dislikeCount > 0;
        dislikeCount -= 1;

        SubComment updatedSubComment = new SubComment(
                subComment.getId(),
                subComment.getContent(),
                subComment.getLikes(),
                dislikeCount,
                subComment.getCreatedDate(),
                subComment.getUpdatedDate(),
                subComment.getAuthor(),
                subComment.getComment()
        );

        when(subCommentRepository.findById("id")).thenReturn(java.util.Optional.of(updatedSubComment));
        when(subCommentRepository.save(updatedSubComment)).thenReturn(updatedSubComment);
        when(subCommentDtoConverter.convert(updatedSubComment)).thenReturn(subCommentDto);

        SubCommentDto result = subCommentService.undislikeSubCommentById("id");

        assertEquals(subCommentDto, result);

        verify(subCommentRepository).findById("id");
        verify(subCommentRepository).save(updatedSubComment);
        verify(subCommentDtoConverter).convert(subComment);
    }


    @AfterEach
    void tearDown() {
    }
}