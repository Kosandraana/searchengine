package searchengine.builder;

import org.apache.logging.log4j.Logger;
import searchengine.config.Props;
import searchengine.model.Site;
import searchengine.repository.Repo;
import searchengine.responses.StatisticResponse;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

import static searchengine.model.StatusType.*;

public class SiteBuilder implements Runnable {
    private static Logger log;
    public static final boolean IS_INDEXING = true;
    private static ExecutorService executor;
    final static int forSitesThreadNumber =
            Props.getInst().getForSitesThreadNumber();
    private static final ConcurrentHashMap<String, Site>
            indexingSites = new ConcurrentHashMap<>();
    private Site site;
    private final Set<String> viewedPages;
    private final ConcurrentLinkedQueue<String> lastNodes;
    private final CopyOnWriteArraySet<String> forbiddenNodes;

    public static ConcurrentHashMap<String, Site> getIndexingSites() {
        return indexingSites;
    }

    public ConcurrentLinkedQueue<String> getLastNodes() {
        return lastNodes;
    }

    public CopyOnWriteArraySet<String> getForbiddenNodes() {
        return forbiddenNodes;
    }

    public Set<String> getViewedPages() {
        return viewedPages;
    }

    private static final boolean SINGLE_SITE_IS_INDEXING = true;
    private static boolean stopping = false;

    public static boolean isStopping() {
        return stopping;
    }

    public static void setStopping(boolean stopping) {
        SiteBuilder.stopping = stopping;
    }

    public SiteBuilder(String siteUrl) {
        lastNodes = new ConcurrentLinkedQueue<>();
        forbiddenNodes = new CopyOnWriteArraySet<>();
        viewedPages = new HashSet<>();

        Optional<Site> indexingSite =
                Repo.siteRepo.findByUrlAndStatus(siteUrl, Site.INDEXING);
        if (!indexingSite.isEmpty()) {
            indexingSite.get().setStatus(REMOVING);
            Repo.siteRepo.saveAndFlush(indexingSite.get());
        }

        site = new Site();
        site.setName(Props.SiteProps.getNameByUrl(siteUrl));
        site.setUrl(siteUrl);
        site.setStatusTime(LocalDateTime.now());
        site.setSiteBuilder(this);
        site.setStatus(INDEXING);

        Repo.siteRepo.saveAndFlush(site);
    }

    @Override
    public void run() {
        log.info("Сайт \"" + site.getName() + "\" индексируется");
        buildPagesLemmasAndIndexes();

        if (isStopping()) {
            Site indexingSite = Repo.siteRepo.findByNameAndStatus(site.getName(), Site.INDEXING)
                    .orElse(null);
            if (indexingSite != null) {
                indexingSite.setStatus(REMOVING);
                Repo.siteRepo.saveAndFlush(indexingSite);
            }
            log.info("Индексация сайта \"" + site.getName() + "\" прервана");
        }

        indexingSites.remove(site.getUrl());
        if (indexingSites.isEmpty()) {
            stopping = false;
        }
    }

    private void buildPagesLemmasAndIndexes() {
        long begin = System.currentTimeMillis();
        Site prevSite;
        PagesOfSiteBuilder.build(site);
        if (isStopping()) {
            return;
        }

        IndexBuilder.build(site);
        if (isStopping()) {
            return;
        }

        log.info(IndexBuilder.TABS + "Сайт \"" + site.getName() + "\" построен за " +
                (System.currentTimeMillis() - begin) / 1000 + " сек");

        setCurrentSiteAsWorking();
    }

    private void setCurrentSiteAsWorking() {
        Site prevSite = Repo.siteRepo.findByNameAndStatus(site.getName(), Site.INDEXED)
                .orElse(null);
        if (prevSite == null) {
            prevSite = Repo.siteRepo.findByNameAndStatus(site.getName(), Site.FAILED)
                    .orElse(null);
        }
        if (prevSite != null) {
            prevSite.setStatus(REMOVING);
            Repo.siteRepo.saveAndFlush(prevSite);
        }

        if (site.getLastError().isEmpty()) {
            site.setStatus(INDEXED);
        } else {
            site.setStatus(FAILED);
        }
        Repo.siteRepo.saveAndFlush(site);

        synchronized(REMOVING) {
            Repo.siteRepo.deleteByStatus(String.valueOf(REMOVING));
        }
    }

    public static void buildSite(String siteUrl) {
        synchronized (Executors.class) {
            if (executor == null) {
                executor = Executors.newFixedThreadPool(forSitesThreadNumber);
            }
        }

        SiteBuilder siteBuilder = new SiteBuilder(siteUrl);

        Site processingSite = indexingSites.putIfAbsent(siteUrl, siteBuilder.site);
        if (processingSite != null) {
            return;
        }

        executor.execute(siteBuilder);
    }

    public static boolean buildAllSites() {
        if (!indexingSites.isEmpty()) {
            return IS_INDEXING;
        }

        List<Props.SiteProps> sitePropsList = Props.getInst().getSites();
        for (var siteProps : sitePropsList) {
            buildSite(siteProps.getUrl());
        }
        return !IS_INDEXING;
    }

    public static void buildSingleSite(String url) {
        String siteName = Props.SiteProps.getNameByUrl(url);
        if (siteName.equals("")) {
            return;
        }
        buildSite(url);
    }

    public static boolean stopIndexing() {
        if (indexingSites.isEmpty()) {
            return !IS_INDEXING;
        }
        setStopping(true);
        return IS_INDEXING;
    }

    public static StatisticResponse getStatistics() {
        return new StatisticResponse();
    }
}

