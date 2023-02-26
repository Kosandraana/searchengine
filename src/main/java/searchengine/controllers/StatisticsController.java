package searchengine.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.builder.SiteBuilder;
import searchengine.responses.Response;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    @GetMapping("/statistics")
    public Response statistics() {
        return SiteBuilder.getStatistics();
    }
}
//    private StatisticsService statistics;
//
//    public StatisticController(StatisticsService statistics) {
//        this.statistics = statistics;
//    }
//    public StatisticController() {
//    }
//
//    @GetMapping("/statistics")
//    public ResponseEntity<?> getStatistics(){
//        StatisticsResponse stat = statistics.getStatistics();
//        return ResponseEntity.ok (stat);
//}