package searchengine.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SiteMap {
    public static final Set<String> noDoubles = new HashSet<>();

    public static Set<String> readJsoup(String strUrl) throws InterruptedException {
        Set<String> result = new LinkedHashSet<>();
        Thread.sleep(100);
        try {
            Document doc = Jsoup.connect(strUrl)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
                    .timeout(10000)
                    .ignoreHttpErrors(true)
                    .ignoreContentType(true)
                    .get();
            Elements elements = doc.select("a");
            for (Element link : elements) {
                String attributeUrl = link.absUrl("href");//все ссылки сайта
                synchronized (noDoubles) {
                    if (attributeUrl.startsWith(strUrl)
                            && noDoubles.add(attributeUrl)
                            && !attributeUrl.contains("#")
                            && !attributeUrl.contains("?")
                            && !attributeUrl.matches("(\\S+(\\.(?i)(jpg|png|gif|bmp|pdf|xml))$)")) {
                        result.add(attributeUrl);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
