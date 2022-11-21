package ru.practicum.exploreclient.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exploreclient.ExploreClient;
import ru.practicum.exploreclient.FeignClientForStats;
import ru.practicum.exploreclient.dto.EndpointHit;
import ru.practicum.exploreclient.dto.StatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация клиента для обмена данными между микросервисами
 */
@Service
public class ExploreClientImpl implements ExploreClient {
    private static final int daysPeriod = 30;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final FeignClientForStats feignClientForStats;

    @Autowired
    public ExploreClientImpl(FeignClientForStats feignClientForStats) {
        this.feignClientForStats = feignClientForStats;
    }

    @Override
    public Map<Long, Long> getViewsForEvents(Collection<Long> eventIds) {
        final String startRange = LocalDateTime.now().minusDays(daysPeriod).format(dateTimeFormatter);
        final String endRange = LocalDateTime.now().format(dateTimeFormatter);
        final String startEncoded = URLEncoder.encode(startRange, StandardCharsets.UTF_8);
        final String endEncoded = URLEncoder.encode(endRange, StandardCharsets.UTF_8);
        final List<String> uris = eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList());

        final List<StatsDto> stats = feignClientForStats.getStats(startEncoded, endEncoded, uris, true);
        final Map<Long, Long> result = new HashMap<>();

        stats.forEach(stat -> result.put(getIdFromUri(stat.getUri()), stat.getHits()));
        return result;
    }

    private Long getIdFromUri(String uri) {
        String[] split = uri.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    @Override
    public void hit(EndpointHit endpointHit) {
        feignClientForStats.hit(endpointHit);
    }
}
