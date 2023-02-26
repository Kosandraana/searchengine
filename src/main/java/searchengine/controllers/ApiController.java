package searchengine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import searchengine.builder.PageBuilder;
import searchengine.builder.SiteBuilder;
import searchengine.config.ServerConfig;
import searchengine.responses.ErrorResponse;
import searchengine.responses.Response;
import searchengine.services.IndexingService;

@RestController
//@Controller
@RequestMapping("/api")
public class ApiController {
    public static final String INDEXING_IS_PROHIBITED = "Индексация на этом сервере запрещена";
    public static final String INDEXING_IS_RUNNING = "Индексация уже запущена";
    public static final String INDEXING_NOT_STARTED = "Индексация не была запущена";
    private final ServerConfig serverConfig;
    public ApiController(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
    @GetMapping("/startIndexing")
    public Response startIndexing() {
        if (!serverConfig.isIndexingAvailable()) {
            return new ErrorResponse(INDEXING_IS_PROHIBITED);
        }
        boolean isIndexing = SiteBuilder.buildAllSites();
        if (isIndexing) {
            return new ErrorResponse(INDEXING_IS_RUNNING);
        }
        return new Response();
    }
    @GetMapping("/stopIndexing")
    public Response stopIndexing() {
        boolean isIndexing = SiteBuilder.stopIndexing();
        if (isIndexing) {
            return new Response();
        }
        return new ErrorResponse(INDEXING_NOT_STARTED);
    }
    @PostMapping("/indexPage")
    public Response indexPage(@RequestParam(required = false) String url) {
        if (!serverConfig.isIndexingAvailable()) {
            return new ErrorResponse(INDEXING_IS_PROHIBITED);
        }
        String result = PageBuilder.indexPage(url);
        if (result.equals(PageBuilder.OK)) {
            return new Response();
        }
        return new ErrorResponse(result);
    }
}

//        private final IndexingService index;
//        public ApiController(IndexingService index) {
//            this.index = index;
//        }
//
//        @GetMapping("/startIndexing")
//        public ResponseEntity<Object> startIndexingAll() {
//            Response response = index.startIndexingAll();
//            return ResponseEntity.ok(response);
//        }
//
//        @GetMapping("/stopIndexing")
//        public ResponseEntity<Object> stopIndexingAll() {
//            Response response = index.stopIndexing();
//            return ResponseEntity.ok(response);
//        }
//
//        @PostMapping("/indexPage")
//        public ResponseEntity<Object> startIndexingOne(
//                @RequestParam(name="url", required=false, defaultValue=" ") String url) {
//            Response response = index.startIndexingOne(url);
//            return ResponseEntity.ok(response);
//        }
//    }

//    private final StatisticsService statistic;
//    private final IndexingService index;
//    private final SearchService search;

//    public ApiController(StatisticsService statistic, IndexingService index, SearchService search) {
//        this.statistic = statistic;
//        this.index = index;
//        this.search = search;
//    }

//    @GetMapping("/startIndexing")
//    public ResponseEntity<?> startIndexing() throws ExecutionException, InterruptedException {
//        Response response = index.startIndexingAll();
//        return ResponseEntity.ok(response);
////        storage.startIndexing();
////        return new ResponseEntity<>(HttpStatus.OK);
////        return indexService.indexingStart(sitesList);
//    }
//
//    @GetMapping("/stopIndexing")
//    public ResponseEntity<?> stopIndexing() throws InterruptedException {
//        Response response = index.stopIndexing();
//        return ResponseEntity.ok(response);
////        return new ResponseEntity<>(HttpStatus.OK);
//    }
//@PostMapping("/indexPage")
//public ResponseEntity<Object> startIndexingOne(
//        @RequestParam(name="url", required=false, defaultValue=" ") String url) {
//    Response response = index.startIndexingOne(url);
//    return ResponseEntity.ok(response);
//}
//    @GetMapping("/statistics")
////    @ResponseBody
//    public ResponseEntity<StatisticsResponse> statistics() {
////        return ResponseEntity.ok(statisticsService.getStatistics());
//        StatisticsResponse stat = statistic.getStatistics();
//        return ResponseEntity.ok(stat);
//    }
//    @GetMapping("/search")
////    @ResponseBody
//    public ResponseEntity<Object> search(
//            @RequestParam(name="query", required=false, defaultValue="") String query,
//            @RequestParam(name="site", required=false, defaultValue="") String site,
//            @RequestParam(name="offset", required=false, defaultValue="0") int offset,
//            @RequestParam(name="limit", required=false, defaultValue="0") int limit) throws IOException {
//        Response service = search.getResponse(new Request(query), site, offset, limit);
//        return ResponseEntity.ok (service);
//    }

