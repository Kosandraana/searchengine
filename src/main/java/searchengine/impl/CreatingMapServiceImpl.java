package searchengine.impl;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.config.Connect;
import searchengine.model.*;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
public class CreatingMapServiceImpl extends RecursiveAction implements CreatingMapService {

    private final Site mainSite;
    private final String root;
    private final Connect connect;
    private final IndexingService indexingService;
    private final SiteRepository sitesRepository;
    private final PageRepository pagesRepository;
    private final IndexRepository indexRepository;
    private final LemmaRepository lemmasRepository;

    @Override
    protected void compute() {
        if (indexingService.isStopFlag())
            return;
        Set<CreatingMapServiceImpl> tasks = new HashSet<>();
        Set<String> pageLinks = parsePage(root);
        for (String link : pageLinks) {
            CreatingMapServiceImpl creatingMapServiceImpl = new CreatingMapServiceImpl(
                    mainSite, link, connect, indexingService,
                    sitesRepository, pagesRepository,
                    indexRepository, lemmasRepository);
            tasks.add(creatingMapServiceImpl);
        }
        for (CreatingMapServiceImpl task : tasks)
            task.fork();
        for (CreatingMapServiceImpl task : tasks)
            task.join();
    }

    private Set<String> parsePage(String url) {
//        long start = System.currentTimeMillis();
//        System.out.println("start parsePage - " + url);
        Set<String> links = new HashSet<>();
        try {
            if (checkURL(url)) {
                Connection connection = Jsoup.connect(url).userAgent(connect.getUserAgent())
                        .referrer(connect.getReferrer()).maxBodySize(0);
                Document doc = connection.get();
                Elements elements = doc.select("a[href]");
                for (Element element : elements) {
                    String link = element.absUrl("href");
                    if (checkURL(link)) {
                        Thread.sleep(200);
                        connection = Jsoup.connect(link).userAgent(connect.getUserAgent())
                                .referrer(connect.getReferrer()).maxBodySize(0);
                        String content = connection.get().toString();
                        int statusCode = connection.execute().statusCode();
                        if (addNewURL(link, statusCode, content)) {
                            links.add(link);
//                            System.out.println("add new link - " + link);
                        }
                    }
                }
                Thread.sleep(200);
            }
        } catch (Exception ex) {
            mainSite.setLastError(ex.toString());
            if (indexingService.isStopFlag()) {
                mainSite.setLastError("Индексация остановлена пользователем");
                mainSite.setStatus(StatusType.FAILED);
            }
            sitesRepository.save(mainSite);
//            ex.printStackTrace();
        }
//        System.out.println("end parsePage - " + url);
//        System.out.println("time working  - " + (System.currentTimeMillis() - start) + " ms");
        return links;
    }

    private boolean checkURL(String url) {
        return url.startsWith(mainSite.getUrl()) && (url.endsWith("/") || url.endsWith(".html"));
    }

    public boolean addNewURL(String url, int statusCode, String content) throws IOException {
        if (indexingService.isStopFlag())
            return false;
        String pathLink = url.substring(mainSite.getUrl().length() - 1);
            Page page = pagesRepository.findByPathLinkAndSite(pathLink, mainSite);

            if (page == null) {
                page = new Page();
                page.setPathLink(pathLink);
                page.setCode(statusCode);
                page.setContent(content);
                page.setSite(mainSite);
                page.setIndexes(new HashSet<>());
                pagesRepository.save(page);
                mainSite.setStatusTime(LocalDateTime.now());
                sitesRepository.save(mainSite);
                if (statusCode < 400) {
                    LemmaFinder creatingLemmas = LemmaFinder.getInstance();
                    Map<String, Integer> mapLemmas = new HashMap<>(creatingLemmas.collectLemmas(content));
                    Set<String> setLemmas = new HashSet<>(mapLemmas.keySet());
                    addLemmasAndIndexes(mapLemmas, setLemmas, page);
                }
                return true;
            }
            return false;
        }

        private synchronized void addLemmasAndIndexes(Map<String, Integer> mapLemmas, Set<String> setLemmas, Page page) {
        for (String newLemma : setLemmas) {
            if (indexingService.isStopFlag())
                return;
            Lemma lemma = lemmasRepository.findByLemmaAndSite(newLemma, mainSite);
            if (lemma == null) {
                lemma = new Lemma();
                lemma.setSite(mainSite);
                lemma.setLemma(newLemma);
                lemma.setFrequency(1);
            } else
                lemma.setFrequency(lemma.getFrequency() + 1);
            lemmasRepository.save(lemma);

            Indexx index = new Indexx();
            index.setPage(page);
            index.setLemmaId(lemma.getId());
            index.setRank(mapLemmas.get(newLemma));
            indexRepository.save(index);
        }
    }

        public synchronized void deleteLemmas (Set<String> setLemmas) {
            for (String newLemma : setLemmas) {
            Lemma lemma = lemmasRepository.findByLemmaAndSite(newLemma, mainSite);
            if (lemma != null) {
                if (lemma.getFrequency() == 1)
                    lemmasRepository.delete(lemma);
                else {
                    lemma.setFrequency(lemma.getFrequency() - 1);
                    lemmasRepository.save(lemma);
                }
            }
        }
    }
}
