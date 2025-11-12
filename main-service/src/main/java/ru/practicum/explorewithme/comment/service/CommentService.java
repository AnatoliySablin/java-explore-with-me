package ru.practicum.explorewithme.comment.service;

import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, NewCommentDto dto);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto dto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> getCommentsByEvent(Long eventId, int from, int size);

    List<CommentDto> getCommentsByUser(Long userId, int from, int size);

    List<CommentDto> getCommentsByAdmin(Long eventId, Long authorId, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, int from, int size);
}
