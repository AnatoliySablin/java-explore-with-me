package ru.practicum.explorewithme.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    Optional<Comment> findByIdAndAuthorId(Long commentId, Long authorId);

    @Query("SELECT c FROM Comment c " +
           "WHERE (:eventId IS NULL OR c.event.id = :eventId) " +
           "AND (:authorId IS NULL OR c.author.id = :authorId) " +
           "AND (CAST(:rangeStart AS timestamp) IS NULL OR c.createdOn >= :rangeStart) " +
           "AND (CAST(:rangeEnd AS timestamp) IS NULL OR c.createdOn <= :rangeEnd)")
    Page<Comment> findCommentsByAdmin(@Param("eventId") Long eventId,
                                       @Param("authorId") Long authorId,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);
}
