//package searchengine.services;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import searchengine.config.SitesList;
//import searchengine.model.Page;
//import searchengine.model.Site;
//import searchengine.repository.LemmaRepository;
//import searchengine.repository.PageRepository;
//import searchengine.repository.SiteRepository;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.ForkJoinTask;
//import java.util.concurrent.RecursiveAction;
//
//public class SiteIndexingAction extends RecursiveAction {
//
//    private String url;
//    private String rootUrl;
//    private Site site;
//    @Autowired
//    private PageRepository pageRepository;
//    @Autowired
//    private SiteRepository siteRepository;
//    @Autowired
//    private LemmaRepository lemmaRepository;
//    @Autowired
//    private SitesList sitesList;
//    private UtilParsing utilParsing;
//    private static final Set<String> allLinks = Collections.synchronizedSet(new HashSet<>());
//
//    List<Thread> threadList = new ArrayList<>();
//    List<ForkJoinPool> forkJoinPoolList = new ArrayList<>();
//
//    public SiteIndexingAction(String url, String rootUrl, Site site, PageRepository pageRepository, SiteRepository siteRepository, UtilParsing utilParsing) {
//        this.url = url;
//        this.rootUrl = rootUrl;
//        this.site = site;
//        this.pageRepository = pageRepository;
//        this.siteRepository = siteRepository;
//        this.utilParsing = utilParsing;
//    }
//    public SiteIndexingAction(String url, String rootUrl) {
//        this.url = url;
//        this.rootUrl = rootUrl;
//    }
////    public boolean startIndexing() {
////        AtomicBoolean indexing = new AtomicBoolean(false);
////    }
//
//    @Override
//    protected void compute() {
//        Set<SiteIndexingAction> tasksList = new HashSet<>();
//        List<String> links = new ArrayList<>();
//        Document document = null;
//        try {
//            Thread.sleep(150);
//            org.jsoup.Connection connection = getConnection(url);
//            Connection.Response response = connection.execute();
//            int statusCode = response.statusCode();
//            if (statusCode == 200) {
//                document = connection.get();
//                Elements elements = (Elements) connection.get().select("a[href]");
//                String content = document.html();
//                for (Element element : elements) {
//                    String absUrl = element.attr("abs:href");
//                    if (isCorrectUrl(absUrl) && !pageRepository.findByPathAndSiteId(absUrl)) {
//                        System.out.println(absUrl);
//                        if (stopped()) {
//                            links.clear();
//                            System.out.println("STOPPED FAILED");
//                            break;
//                        }
//                        Page page = new Page(absUrl, statusCode, content);
//                        pageRepository.save(page);
//                        site.setStatusTime(LocalDateTime.now());
//                        siteRepository.save(site);
//                        links.add(absUrl);
//                        allLinks.add(absUrl);
//                        if (stopped()) {
//                            break;
//                        }
//                    }
//                }
//            }
//        } catch (IOException | InterruptedException exception) {
//            exception.printStackTrace();
//        }
//        for (String link : links) {
//            SiteIndexingAction task = new SiteIndexingAction(link, rootUrl);
//            task.fork();
//            tasksList.add(task);
//        }
//        tasksList.forEach(ForkJoinTask::join);
//        System.out.println(allLinks.size());
//        allLinks.clear();
//    }
//    public boolean isCorrectUrl(String url) {
//        return !url.isEmpty() &&
//                url.startsWith(rootUrl) &&
//                !allLinks.contains("#") &&
//                !url.contains("jpg") &&
//                !url.contains("?method=");
//    }
//
//    public org.jsoup.Connection getConnection(String url) {
//        org.jsoup.Connection connection = Jsoup.connect(url)
//                .ignoreContentType(true)
//                .ignoreHttpErrors(true)
//                .followRedirects(false)
//                .userAgent("Mozilla/5.0 (Windows; U; Windows 5.1; en-US; " +
//                        "rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6.")
//                .referrer("http://www.google.com")
//                .timeout(10000);
//        return connection;
//    }
//
//    public boolean stopped() {
//        return utilParsing.isStopped();
//    }
//}
