package ru.practicum.explorewithme.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "author", source = "author")
    CommentDto toDto(Comment comment);
}
