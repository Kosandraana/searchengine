package searchengine.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import searchengine.dto.statistics.StatisticsResponse;

    @Data
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    public class StatisticResponse extends Response {
        //        private Statistics statistics = new Statistics();
        private boolean result;
        StatisticsResponse statistics;

        public StatisticResponse(boolean result, StatisticsResponse statistics) {
            this.statistics = statistics;
            this.result = result;
        }
        public StatisticResponse() {
        }
    }