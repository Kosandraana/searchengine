//package searchengine.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//import searchengine.config.SitesList;
//import searchengine.dto.statistics.DetailedStatisticsItem;
//import searchengine.dto.statistics.StatisticsData;
//import searchengine.dto.statistics.StatisticsResponse;
//import searchengine.dto.statistics.TotalStatistics;
//import searchengine.model.Lemma;
//import searchengine.model.Page;
//import searchengine.model.Site;
//import searchengine.model.StatusType;
//import searchengine.responses.StatisticResponse;
//import searchengine.services.*;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Service
//@Primary
//@RequiredArgsConstructor
//public class StatisticsServiceImpl implements StatisticsService {
//
//    private final Random random = new Random();
//    private final SitesList sites;
//
//    @Override
//    public StatisticsResponse getStatistics() {
//        String[] statuses = {"INDEXED", "FAILED", "INDEXING"};
//        String[] errors = {
//                "Ошибка индексации: главная страница сайта не доступна",
//                "Ошибка индексации: сайт не доступен",
//                ""
//        };
//
////        TotalStatistics total = new TotalStatistics(sites, pages, lemmas, isIndexing);
//        TotalStatistics total = new TotalStatistics();
//        total.setSites(sites.getSites().size());
//        total.setIndexing(true);
//
//        List<DetailedStatisticsItem> detailed = new ArrayList<>();
//        List<Site> sitesList = sites.getSites();
//        for (int i = 0; i < sitesList.size(); i++) {
//            Site site = sitesList.get(i);
////            DetailedStatisticsItem item = new DetailedStatisticsItem(url, name, status, statusTime, error, pages, lemmas);
//            DetailedStatisticsItem item = new DetailedStatisticsItem();
//            item.setName(site.getName());
//            item.setUrl(site.getUrl());
//            int pages = random.nextInt(1_000);
//            int lemmas = pages * random.nextInt(1_000);
//            item.setPages(pages);
//            item.setLemmas(lemmas);
//            item.setStatus(StatusType.valueOf(statuses[i % 3]));
//            item.setError(errors[i % 3]);
//            item.setStatusTime(System.currentTimeMillis() -
//                    (random.nextInt(10_000)));
//            total.setPages(total.getPages() + pages);
//            total.setLemmas(total.getLemmas() + lemmas);
//            detailed.add(item);
//        }
//
//        StatisticsResponse response = new StatisticsResponse();
//        StatisticsData data = new StatisticsData();
//        data.setTotal(total);
//        data.setDetailed(detailed);
//        response.setTotalStatistics(data.getTotal());
//        response.setResult(true);
//        return response;
//    }
//}
//
////    private static final Log log = LogFactory.getLog(StatisticsServiceImpl.class);
////
////    private final SiteRepositoryService siteRepositoryService;
////    private final LemmaRepositoryService lemmaRepositoryService;
////    private final PageRepositoryService pageRepositoryService;
////
////    public StatisticsServiceImpl(SiteRepositoryService siteRepositoryService,
////                      LemmaRepositoryService lemmaRepositoryService,
////                      PageRepositoryService pageRepositoryService, SitesList sites) {
////        this.siteRepositoryService = siteRepositoryService;
////        this.lemmaRepositoryService = lemmaRepositoryService;
////        this.pageRepositoryService = pageRepositoryService;
////        this.sites = sites;
////    }
////
//////    public StatisticsResponse getStatistics(){
//////        TotalStatistics total = getTotal();
//////        List<Site> siteList = siteRepositoryService.getAllSites();
//////        DetailedStatisticsItem[] detaileds = new DetailedStatisticsItem[siteList.size()];
//////        for (int i = 0; i < siteList.size(); i++) {
//////            detaileds[i] = getDetailed(siteList.get(i));
//////        }
//////        log.info("Получение статистики.");
//////        return new StatisticResponse(true, new StatisticsResponse(total, detaileds)).getStatistics();
//////    }
////
////    private TotalStatistics getTotal(){
////        long sites = siteRepositoryService.siteCount();
////        long lemmas = lemmaRepositoryService.lemmaCount();
////        long pages = pageRepositoryService.pageCount();
////        boolean isIndexing = isSitesIndexing();
////        return new TotalStatistics(sites, pages, lemmas, isIndexing);
////
////    }
////
////    private DetailedStatisticsItem getDetailed(Site site){
////        String url = site.getUrl();
////        String name = site.getName();
////        StatusType status = site.getStatus();
////        long statusTime = site.getStatusTime();
////        String error = site.getLastError();
////        long pages = pageRepositoryService.pageCount(site.getId());
////        long lemmas = lemmaRepositoryService.lemmaCount(site.getId());
////        return new DetailedStatisticsItem(url, name, status, statusTime, error, pages, lemmas);
////    }
////
////    private boolean isSitesIndexing(){
////        boolean is = true;
////        for(Site s : siteRepositoryService.getAllSites()){
////            if(!s.getStatus().equals(StatusType.INDEXED)){
////                is = false;
////                break;
////            }
////        }
////        return is;
////    }
////}
