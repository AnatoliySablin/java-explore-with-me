package ru.practicum.explorewithme.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate;
    private final String serverUrl;

    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;
        this.restTemplate = builder
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public void saveHit(EndpointHitDto endpointHitDto) {
        restTemplate.postForEntity(serverUrl + "/hit", endpointHitDto, Object.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(serverUrl + "/stats")
                .queryParam("start", encodeDate(start))
                .queryParam("end", encodeDate(end));

        if (uris != null && !uris.isEmpty()) {
            uriBuilder.queryParam("uris", uris.toArray());
        }

        if (unique != null && unique) {
            uriBuilder.queryParam("unique", true);
        }

        String uri = uriBuilder.toUriString();
        ResponseEntity<ViewStatsDto[]> response = restTemplate.getForEntity(uri, ViewStatsDto[].class);

        return response.getBody() != null ? List.of(response.getBody()) : List.of();
    }

    private String encodeDate(LocalDateTime dateTime) {
        return URLEncoder.encode(dateTime.format(FORMATTER), StandardCharsets.UTF_8);
    }
}

