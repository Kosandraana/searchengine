package searchengine.builder;

import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import searchengine.config.Props;
import searchengine.model.Indexx;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.Repo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBuilder implements Runnable {
    private static Logger log;
    public static final String OK = "OK";
    public static final String NOT_FOUND = "\"Данная страница находится за пределами сайтов, " +
            "указанных в конфигурационном файле";
    public static final String SITE_NOT_INDEXED = "Нельзя индексировать страницу " +
            "сайта, если сайт ещё не индексирован";
    public static final String RUNNING = "Индексация уже запущена";

    private final Site site;
    private List<Page> oldPages;
    private Page page = null;

    public PageBuilder(Site site, String pagePath) {
        this.site = site;
        oldPages = Repo.pageRepo.findAllBySiteAndPathAndCode(site, pagePath, Node.OK);

        Node node = new Node(site, pagePath);
        node.setFromPageBuilder(true);
        Document doc = node.processAndReturnPageDoc();
        if (doc == null) {
            return;
        }
        int id = node.getAddedPageId();
        page = Repo.pageRepo.findById(id).orElse(null);
        if (page == null) {
            doc = null;
            return;
        }
        page.setContent(doc.outerHtml());
        page.setPath(pagePath);
    }

    @Override
    public void run() {
        log.info("Проиндексирована страница " + site.getUrl() + page.getPath());
        List<Lemma> lemmaList = Repo.lemmaRepo.findAllBySite(site);
        Map<String, Lemma> lemmas = new HashMap<>();
        for (Lemma lemma : lemmaList) {
            lemmas.put(lemma.getLemma(), lemma);
        }

        List<Indexx> indexList = Repo.indexRepo.findAllBySite(site);
        Map<Integer, Indexx> indexes = new HashMap<>();
        for (Indexx index : indexList) {
            indexes.put(index.hashCode(), index);
        }

        IndexBuilder indexBuilder = new IndexBuilder(site, page, lemmas, indexes);
        indexBuilder.fillLemmasAndIndices();

        List<Lemma> lemmasToDelete = new ArrayList<>();
        if (oldPages != null && oldPages.size() > 0) {
            List<Integer> oldPageIds = oldPages.stream().map(p -> p.getId()).toList();
            for (Indexx indexx : indexes.values().stream()
                    .filter(indexx -> oldPageIds.contains(indexx.getPage().getId()))
                    .toList()) {
                Lemma lemma = indexx.getLemma();
                lemma.setFrequency(lemma.getFrequency() - 1);
                if (lemma.getFrequency() == 0) {
                    lemmas.remove(lemma.getLemma());
                    lemmasToDelete.add(lemma);
                }
            }
        }
        Repo.lemmaRepo.deleteAllInBatch(lemmasToDelete);

        List<Indexx> pageIndexes = new ArrayList<>();
        pageIndexes.addAll(indexes.values().stream()
                .filter(indexx -> indexx.getPage().getId() == page.getId())
                .toList());

        synchronized (Page.class) {
            Repo.pageRepo.saveAndFlush(page);
        }
        Repo.lemmaRepo.deleteAllInBatch(lemmasToDelete);
        Repo.lemmaRepo.saveAllAndFlush(lemmaList);
        Repo.indexRepo.saveAllAndFlush(pageIndexes);
        synchronized (Page.class) {
            if (oldPages != null) {
                for (Page p : oldPages) {
                    Repo.pageRepo.deleteById(p.getId());
                }
            }
        }

        SiteBuilder.getIndexingSites().remove(site.getUrl());
    }

    public static String indexPage(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            return NOT_FOUND;
        }
        String home = url.getProtocol() + "://" + url.getHost();
        String path = url.getFile();

        if (SiteBuilder.getIndexingSites().containsKey(home)) {
            return RUNNING;
        }

        if (!Props.getAllSiteUrls().contains(home)) {
            return NOT_FOUND;
        }
        Site site = Repo.siteRepo.findByUrlAndStatus(home, Site.INDEXED).orElse(null);

        if (path.isEmpty()) {
            SiteBuilder.buildSingleSite(home);
        } else {
            if (site == null) {
                return SITE_NOT_INDEXED;
            }
            PageBuilder pageBuilder = new PageBuilder(site, path);
            if (pageBuilder.page == null) {
                return NOT_FOUND;
            }
            SiteBuilder.getIndexingSites().put(site.getUrl(), site);
            pageBuilder.run();
        }

        return OK;
    }
}

