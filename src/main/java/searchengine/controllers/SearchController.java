package searchengine.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import searchengine.responses.ErrorResponse;
import searchengine.responses.Response;
import searchengine.responses.SearchResponse;
import searchengine.search.SearchListener;
import searchengine.search.SearchRequest;

@RestController
@Slf4j
//@Controller
@RequestMapping("/api")
public class SearchController {

//    private final SearchService search;
//    private static Logger log;

//    public SearchController(SearchService search) {
//        this.search = search;
//    }
//    @GetMapping("/search")
//    public ResponseEntity<Object> search(
//            @RequestParam(name="query", required=false, defaultValue="") String query,
//            @RequestParam(name="site", required=false, defaultValue="") String site,
//            @RequestParam(name="offset", required=false, defaultValue="0") int offset,
//            @RequestParam(name="limit", required=false, defaultValue="0") int limit) throws IOException {
//            log.info("Поисковый запрос: " + query);
//            SearchRequest request = new SearchRequest().buildRequest(query, site, offset, limit);
//            if (request == null) {
//                return new ErrorResponse("Задан пустой поисковый запрос");
//            }
//            log.info("Леммы: " + request.getQueryWords());
//            return receiveResponse(request);
//        }
//        Response service = search.getResponse(new Request(query), site, offset, limit);
//        return ResponseEntity.ok (service);
@GetMapping("/search")
public Response search(@RequestParam(required = false) String query,
                       @RequestParam(required = false) String site,
                       @RequestParam(required = false) Integer offset,
                       @RequestParam(required = false) Integer limit) {
    log.info("Поисковый запрос: " + query);
    SearchRequest request = new SearchRequest().buildRequest(query, site, offset, limit);
    if (request == null) {
        return new ErrorResponse("Задан пустой поисковый запрос");
    }
    log.info("Леммы: " + request.getQueryWords());
    return receiveResponse(request);
}
    private Response receiveResponse(SearchRequest request) {
        SearchResponse response;
        try {
            SearchListener.getRequestQueue().put(request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            response = SearchListener.getResponseQueue().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}