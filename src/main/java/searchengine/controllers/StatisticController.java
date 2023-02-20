//package searchengine.controllers;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import searchengine.dto.statistics.StatisticsResponse;
//import searchengine.services.StatisticsService;
//
//@Controller
//public class StatisticController {
//
//    private StatisticsService statistic;
//
//    public StatisticController(StatisticsService statistic) {
//        this.statistic = statistic;
//    }
//    public StatisticController() {
//
//    }
//
//    @GetMapping("/statistics")
//    public ResponseEntity<?> getStatistics(){
//        StatisticsResponse stat = statistic.getStatistics();
//        return ResponseEntity.ok (stat);
//    }
//}