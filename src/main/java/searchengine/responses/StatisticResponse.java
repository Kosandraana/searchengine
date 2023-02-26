package searchengine.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import searchengine.builder.SiteBuilder;
import searchengine.model.Site;
import searchengine.model.StatusType;
import searchengine.repository.Repo;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static searchengine.model.StatusType.*;

@Data
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false)
    public class StatisticResponse extends Response {
//        //        private Statistics statistics = new Statistics();
//        private boolean result;
//        StatisticsResponse statistics;
//
//        public StatisticResponse(boolean result, StatisticsResponse statistics) {
//            this.statistics = statistics;
//            this.result = result;
//        }
//        public StatisticResponse() {
//        }

    private Statistics statistics = new Statistics();
    @Data
    static class Statistics {
        private TotalStatistics total;
        private List<DetailedStatistics> detailed;
        public Statistics() {
            total = new TotalStatistics();
            detailed = new ArrayList<>();

            List<Site> sites = Repo.siteRepo.findAll().stream()
                    .filter(site -> site.getStatus().equals(INDEXED) ||
                            site.getStatus().equals(FAILED) ||
                            site.getStatus().equals(INDEXING))
                    .toList();
            for (Site site : sites) {
                DetailedStatistics detailedStatistics = new DetailedStatistics(site);
                detailed.add(detailedStatistics);
            }
        }
    }
    @Data
    static class TotalStatistics {
        private int sites;
        private int pages;
        private int lemmas;
        private boolean isIndexing;
        public TotalStatistics() {
            int siteCount = Repo.siteRepo.countByStatus(Site.INDEXED) +
                    Repo.siteRepo.countByStatus(Site.FAILED);
            setSites(siteCount);

            List<Site> indexedSites = Repo.siteRepo.findAllByStatus(Site.INDEXED);
            setPages(Repo.pageRepo.countBySites(indexedSites));
            setLemmas(Repo.lemmaRepo.countBySites(indexedSites));

            setIndexing(!SiteBuilder.getIndexingSites().isEmpty());
        }
    }
    @Data
    static class DetailedStatistics {
        private String url;
        private String name;
        private StatusType status;
        private long statusTime;
        private String error;
        private int pages;
        private int lemmas;

        public DetailedStatistics(Site site) {
            url = site.getUrl();
            name = site.getName();
            status = site.getStatus();
            statusTime = (site.getStatusTime().toEpochSecond(ZoneOffset.UTC) - 3 * 3600) * 1000;
            error = site.getLastError();
            pages = Repo.pageRepo.countBySite(site);
            lemmas = Repo.lemmaRepo.countBySite(site);
        }
    }
}