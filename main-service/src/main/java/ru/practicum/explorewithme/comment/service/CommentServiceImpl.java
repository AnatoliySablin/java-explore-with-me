package ru.practicum.explorewithme.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentDto;
import ru.practicum.explorewithme.comment.mapper.CommentMapper;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.comment.repository.CommentRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.event.model.EventState;
import ru.practicum.explorewithme.event.repository.EventRepository;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.user.model.User;
import ru.practicum.explorewithme.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot comment on unpublished event");
        }

        Comment comment = Comment.builder()
                .text(dto.getText())
                .event(event)
                .author(user)
                .createdOn(LocalDateTime.now())
                .build();

        Comment saved = commentRepository.save(comment);
        log.info("Created comment: {}", saved);
        return commentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto dto) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found or user is not author: " + commentId));

        comment.setText(dto.getText());
        comment.setUpdatedOn(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        log.info("Updated comment: {}", updated);
        return commentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment not found or user is not author: " + commentId));

        commentRepository.delete(comment);
        log.info("Deleted comment by user {}: {}", userId, commentId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));

        commentRepository.delete(comment);
        log.info("Deleted comment by admin: {}", commentId);
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot view comments for unpublished event");
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return commentRepository.findByEventId(eventId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByUser(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found: " + userId);
        }

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return commentRepository.findByAuthorId(userId, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(Long eventId, Long authorId, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return commentRepository.findCommentsByAdmin(eventId, authorId, rangeStart, rangeEnd, pageable).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
