package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.dto.search.SearchResponse;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.impl.IndexingService;
import searchengine.impl.SearchService;
import searchengine.impl.StatisticsService;

// http://localhost:8080/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final SearchService searchService;
    private final IndexingService indexingService;
    private final StatisticsService statisticsService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingResponse> startIndexing() {
        return ResponseEntity.ok(indexingService.getStartIndexing());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingResponse> stopIndexing() {
        return ResponseEntity.ok(indexingService.getStopIndexing());
    }
    @PostMapping("/indexPage")
    public ResponseEntity<IndexingResponse> indexPage(@RequestParam(name = "url") String url) {
        return ResponseEntity.ok(indexingService.getIndexingPage(url));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "site", required = false) String site,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return ResponseEntity.ok(searchService.getSearch(query, site, offset, limit));
    }
}