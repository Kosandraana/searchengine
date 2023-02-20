package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import searchengine.config.Request;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.responses.Response;
import searchengine.services.IndexingService;
import searchengine.services.SearchService;
import searchengine.services.StatisticsService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//@RestController
@Controller
@RequiredArgsConstructor
//@RequestMapping("/api")
public class ApiController {

    private final StatisticsService statistic;
    private final IndexingService index;
    private final SearchService search;

//    public ApiController(StatisticsService statistic, IndexingService index, SearchService search) {
//        this.statistic = statistic;
//        this.index = index;
//        this.search = search;
//    }

    @GetMapping("/startIndexing")
    public ResponseEntity<?> startIndexing() throws ExecutionException, InterruptedException {
        Response response = index.startIndexingAll();
        return ResponseEntity.ok(response);
//        storage.startIndexing();
//        return new ResponseEntity<>(HttpStatus.OK);
//        return indexService.indexingStart(sitesList);
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<?> stopIndexing() throws InterruptedException {
        Response response = index.stopIndexing();
        return ResponseEntity.ok(response);
//        return new ResponseEntity<>(HttpStatus.OK);
    }
@PostMapping("/indexPage")
public ResponseEntity<Object> startIndexingOne(
        @RequestParam(name="url", required=false, defaultValue=" ") String url) {
    Response response = index.startIndexingOne(url);
    return ResponseEntity.ok(response);
}
    @GetMapping("/statistics")
    @ResponseBody
    public ResponseEntity<StatisticsResponse> statistics() {
//        return ResponseEntity.ok(statisticsService.getStatistics());
        StatisticsResponse stat = statistic.getStatistics();
        return ResponseEntity.ok(stat);
    }
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<Object> search(
            @RequestParam(name="query", required=false, defaultValue="") String query,
            @RequestParam(name="site", required=false, defaultValue="") String site,
            @RequestParam(name="offset", required=false, defaultValue="0") int offset,
            @RequestParam(name="limit", required=false, defaultValue="0") int limit) throws IOException {
        Response service = search.getResponse(new Request(query), site, offset, limit);
        return ResponseEntity.ok (service);
    }
}
