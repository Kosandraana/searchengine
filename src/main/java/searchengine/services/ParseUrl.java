package searchengine.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.RecursiveTask;


public class ParseUrl extends RecursiveTask<String> {
    public final static List<String> urlList = new Vector<>();

    private final static Log log = LogFactory.getLog(ParseUrl.class);
    private final String url;
    private final boolean isInterrupted;

    public ParseUrl(String url, boolean isInterrupted) {
        this.url = url;
        this.isInterrupted = isInterrupted;
    }

    @Override
    protected String compute() {
        if(isInterrupted){
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(url);
        try {
            Thread.sleep(200);
            Document doc = getDocumentByUrl(url);
            Elements rootElements = doc.select("a");

            List<ParseUrl> linkGrabers = new ArrayList<>();
            rootElements.forEach(element -> {
                String link = element.attr("abs:href");
                if (link.startsWith(element.baseUri())
                        && !link.equals(element.baseUri())
                        && !link.contains("#")
                        && !link.contains(".pdf")
                        && !urlList.contains(link)
                ) {
                    urlList.add(link);
                    ParseUrl linkGraber = new ParseUrl(link, false);
                    linkGraber.fork();
                    linkGrabers.add(linkGraber);
                }
            });

            for (ParseUrl lg : linkGrabers) {
                String text = lg.join();
                if (!text.equals("")) {
                    result.append("\n");
                    result.append(text);
                }
            }
        } catch (IOException | InterruptedException e) {
            log.warn("Ошибка парсинга сайта: " + url);
        }
        return result.toString();
    }

    protected Document getDocumentByUrl (String url) throws InterruptedException, IOException {
        Thread.sleep(200);
        return Jsoup.connect(url)
                .maxBodySize(0)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("http://www.google.com")
                .get();
    }
}


//public class ParseUrl extends RecursiveTask<Set<String>> {
//    String url;
//    SiteRepository searchengine.repository;
//    Site site;
//
//    public ParseUrl(String url) {
//        this.url = url;
//    }
//    public void setSite(Site site) {
//        this.site = site;
//    }
//    public void setRepository(SiteRepository searchengine.repository) {
//        this.searchengine.repository = searchengine.repository;
//    }
//
////    public boolean isCorrectUrl(String path) {
////        if (!path.isEmpty() && path.startsWith(url) &&
////        !links.contains(path) && !path.contains("#")) {
////            return true;
////        }
////        return false;
////    }
//
//    public Connection getConnection(String url) {
//        Connection connection = (Connection) Jsoup.connect(url)
//                .ignoreContentType(true)
//                .ignoreHttpErrors(true)
//                .followRedirects(false)
//                .userAgent("Mozilla/5.0 (Windows; U; Windows 5.1; en-US; " +
//                        "rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6.")
//                .referrer("http://www.google.com")
//                .timeout(10000);
//                return connection;
//    }
//
////    protected Set<String> compute() {
////        try {
////            Thread.sleep(150);
////        } catch (InterruptedException exception) {
////            exception.printStackTrace();
////        }
////        try {
////            Document document = getDocument(url);
////            List<String> urlList = new ArrayList<>(MyUtils.getSiteMap(url, site));
////            List<ParseUrl> taskList = MyUtils.taskList(urlList);
////            for (ParseUrl task : taskList) {
////                task.fork();
////            }
////            for (ParseUrl task : taskList) {
////                MyUtils.noDoubleUrlList.addAll(task.join());
////            }
////        } catch (ConcurrentModificationException exception) {
////            exception.printStackTrace();
////        }
////        return MyUtils.noDoubleUrlList;
////    }
//}
