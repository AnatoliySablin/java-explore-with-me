package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final EndpointHitMapper mapper;

    @Override
    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = mapper.toEntity(endpointHitDto);
        EndpointHit saved = statsRepository.save(endpointHit);
        log.info("Saved hit: {}", saved);
        return mapper.toDto(saved);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<String> urisParam = (uris == null || uris.isEmpty()) ? null : uris;
        if (unique != null && unique) {
            return statsRepository.findStatsUnique(start, end, urisParam);
        }
        return statsRepository.findStats(start, end, urisParam);
    }
}

