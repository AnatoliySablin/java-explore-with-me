package ru.practicum.explorewithme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StatsServiceImplTest {

    @Autowired
    private StatsService statsService;

    private EndpointHitDto hitDto;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        hitDto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.1.1")
                .timestamp(now)
                .build();
    }

    @Test
    void saveHit() {
        EndpointHitDto saved = statsService.saveHit(hitDto);
        assertThat(saved.getId(), notNullValue());
        assertThat(saved.getApp(), equalTo(hitDto.getApp()));
        assertThat(saved.getUri(), equalTo(hitDto.getUri()));
        assertThat(saved.getIp(), equalTo(hitDto.getIp()));
    }

    @Test
    void getStats() {
        statsService.saveHit(hitDto);

        List<ViewStatsDto> stats = statsService.getStats(
                now.minusHours(1),
                now.plusHours(1),
                List.of("/events/1"),
                false
        );

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getApp(), equalTo("ewm-main-service"));
        assertThat(stats.get(0).getUri(), equalTo("/events/1"));
        assertThat(stats.get(0).getHits(), equalTo(1L));
    }

    @Test
    void getStatsUnique() {
        statsService.saveHit(hitDto);
        statsService.saveHit(hitDto);

        List<ViewStatsDto> statsAll = statsService.getStats(
                now.minusHours(1),
                now.plusHours(1),
                null,
                false
        );
        assertThat(statsAll.get(0).getHits(), equalTo(2L));

        List<ViewStatsDto> statsUnique = statsService.getStats(
                now.minusHours(1),
                now.plusHours(1),
                null,
                true
        );
        assertThat(statsUnique.get(0).getHits(), equalTo(1L));
    }
}

