package searchengine.dto.statistics;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@Data
public class TotalStatistics {
     long sites;
     long pages;
     long lemmas;
     boolean isIndexing;

    public TotalStatistics(long sites, long pages, long lemmas, boolean isIndexing) {
        this.sites = sites;
        this.pages = pages;
        this.lemmas = lemmas;
        this.isIndexing = isIndexing;
    }
    public TotalStatistics() {
    }
}
