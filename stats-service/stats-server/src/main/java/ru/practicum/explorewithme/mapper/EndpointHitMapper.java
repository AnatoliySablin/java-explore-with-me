package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    EndpointHitDto toDto(EndpointHit endpointHit);

    EndpointHit toEntity(EndpointHitDto endpointHitDto);
}

