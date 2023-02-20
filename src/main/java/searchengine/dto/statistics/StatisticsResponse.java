package searchengine.dto.statistics;

import lombok.*;
import searchengine.responses.Response;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class StatisticsResponse extends Response {
//    private boolean result;
//    private StatisticsData statistics;
    TotalStatistics totalStatistics;
    DetailedStatisticsItem[] detailed;

//    public StatisticsResponse(boolean result, StatisticsData statistics, TotalStatistics totalStatistics, DetailedStatisticsItem[] detailed) {
//        this.result = result;
//        this.statistics = statistics;
//        this.totalStatistics = totalStatistics;
//        this.detailed = detailed;
//    }

    public StatisticsResponse() {
    }
    public StatisticsResponse(TotalStatistics totalStatistics, DetailedStatisticsItem[] detailed) {
        this.totalStatistics = totalStatistics;
        this.detailed = detailed;
    }
}
