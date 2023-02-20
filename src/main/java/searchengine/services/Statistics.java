package searchengine.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import searchengine.dto.statistics.DetailedStatisticsItem;

import searchengine.dto.statistics.StatisticsResponse;
import searchengine.dto.statistics.TotalStatistics;
import searchengine.model.Site;
import searchengine.model.StatusType;
import searchengine.responses.StatisticResponse;

import java.util.List;

@Service
//@Qualifier
public class Statistics implements StatisticsService {

    private static final Log log = LogFactory.getLog(Statistics.class);

    private final SiteRepositoryService siteRepositoryService;
    private final LemmaRepositoryService lemmaRepositoryService;
    private final PageRepositoryService pageRepositoryService;

    public Statistics(SiteRepositoryService siteRepositoryService,
                     LemmaRepositoryService lemmaRepositoryService,
                     PageRepositoryService pageRepositoryService) {
        this.siteRepositoryService = siteRepositoryService;
        this.lemmaRepositoryService = lemmaRepositoryService;
        this.pageRepositoryService = pageRepositoryService;
    }

    public StatisticsResponse getStatistics(){
        TotalStatistics total = getTotal();
        List<Site> siteList = siteRepositoryService.getAllSites();
        DetailedStatisticsItem[] detaileds = new DetailedStatisticsItem[siteList.size()];
        for (int i = 0; i < siteList.size(); i++) {
            detaileds[i] = getDetailed(siteList.get(i));
        }
        log.info("Получение статистики.");
        return new StatisticResponse(true, new StatisticsResponse(total, detaileds)).getStatistics();
    }

    private TotalStatistics getTotal(){
        long sites = siteRepositoryService.siteCount();
        long lemmas = lemmaRepositoryService.lemmaCount();
        long pages = pageRepositoryService.pageCount();
        boolean isIndexing = isSitesIndexing();
        return new TotalStatistics(sites, pages, lemmas, isIndexing);

    }

    private DetailedStatisticsItem getDetailed(Site site){
        String url = site.getUrl();
        String name = site.getName();
        StatusType status = site.getStatus();
        long statusTime = site.getStatusTime();
        String error = site.getLastError();
        long pages = pageRepositoryService.pageCount(site.getId());
        long lemmas = lemmaRepositoryService.lemmaCount(site.getId());
        return new DetailedStatisticsItem(url, name, status, statusTime, error, pages, lemmas);
    }

    private boolean isSitesIndexing(){
        boolean is = true;
        for(Site s : siteRepositoryService.getAllSites()){
            if(!s.getStatus().equals(StatusType.INDEXED)){
                is = false;
                break;
            }
        }
        return is;
    }
}
