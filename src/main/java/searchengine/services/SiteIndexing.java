//package searchengine.services;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.UnsupportedMimeTypeException;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import searchengine.impl.IndexRepositoryService;
//import searchengine.impl.LemmaRepositoryService;
//import searchengine.impl.PageRepositoryService;
//import searchengine.impl.SiteRepositoryService;
//import searchengine.model.*;
//
//import java.io.IOException;
//import java.util.*;
//
//public class SiteIndexing extends Thread{
//    private final Site site;
//    private final SearchSettings searchSettings;
////    private final FieldRepositoryService fieldRepositoryService;
//    private final SiteRepositoryService siteRepositoryService;
//    private final IndexRepositoryService indexRepositoryService;
//    private final PageRepositoryService pageRepositoryService;
//    private final LemmaRepositoryService lemmaRepositoryService;
//    private final boolean allSite;
//
//    public SiteIndexing(Site site,
//                        SearchSettings searchSettings,
//                        SiteRepositoryService siteRepositoryService,
//                        IndexRepositoryService indexRepositoryService,
//                        PageRepositoryService pageRepositoryService,
//                        LemmaRepositoryService lemmaRepositoryService,
//                        boolean allSite) {
//        this.site = site;
//        this.searchSettings = searchSettings;
////        this.fieldRepositoryService = fieldRepositoryService;
//        this.siteRepositoryService = siteRepositoryService;
//        this.indexRepositoryService = indexRepositoryService;
//        this.pageRepositoryService = pageRepositoryService;
//        this.lemmaRepositoryService = lemmaRepositoryService;
//        this.allSite = allSite;
//    }
//
//    @Override
//    public void run() {
//        try {
//            if (allSite) {
//                runAllIndexing();
//            } else {
//                runOneSiteIndexing(site.getUrl());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void runAllIndexing() {
//        site.setStatus(StatusType.INDEXING);
//        site.setStatusTime(new Date().getTime());
//        siteRepositoryService.save(site);
//        SiteMapBuilder builder = new SiteMapBuilder(site.getUrl(), this.isInterrupted());
//        builder.builtSiteMap();
//        List<String> allSiteUrls = builder.getSiteMap();
//        for(String url : allSiteUrls) {
//            runOneSiteIndexing(url);
//        }
//    }
//
//    public void runOneSiteIndexing(String searchUrl) {
//        site.setStatus(StatusType.INDEXING);
//        site.setStatusTime(new Date().getTime());
//        siteRepositoryService.save(site);
////        List<Field> fieldList = getFieldListFromDB();
//        List<Site> siteList = getSiteListFromDB();
//        try {
//            Page page = getSearchPage(searchUrl, site.getUrl(), site.getId());
//            Page checkPage = pageRepositoryService.getPage(searchUrl.replaceAll(site.getUrl(), ""));
//            if (checkPage != null){
//                prepareDbToIndexing(checkPage);
//            }
//            TreeMap<String, Integer> map = new TreeMap<>();
//            TreeMap<String, Float> indexing = new TreeMap<>();
////            for (Field field : fieldList){
////                String name = field.getName();
////                float weight = field.getWeight();
//            for(Site site1 : siteList) {
//                String name = site1.getName();
//                String stringByTeg = getStringByTeg(name, page.getContent());
//                MorphologyAnalyzer analyzer = new MorphologyAnalyzer();
//                TreeMap<String, Integer> tempMap = analyzer.textAnalyzer(stringByTeg);
//                map.putAll(tempMap);
//                indexing.putAll(indexingLemmas(tempMap));
//            }
//            lemmaToDB(map, site.getId());
//            map.clear();
//            pageToDb(page);
//            indexingToDb(indexing, page.getPath());
//            indexing.clear();
//        }
//        catch (UnsupportedMimeTypeException e) {
//            site.setLastError("Формат страницы не поддерживается: " + searchUrl);
//            site.setStatus(StatusType.FAILED);
//        }
//        catch (IOException e) {
//            site.setLastError("Ошибка чтения страницы: " + searchUrl + "\n" + e.getMessage());
//            site.setStatus(StatusType.FAILED);
//        }
//        finally {
//            siteRepositoryService.save(site);
//        }
//        site.setStatus(StatusType.INDEXED);
//        siteRepositoryService.save(site);
//    }
//
//
//    private void pageToDb(Page page) {
//        pageRepositoryService.save(page);
//    }
//
//    private Page getSearchPage(String url, String baseUrl, int siteId) throws IOException {
//        Page page = new Page();
//        Connection.Response response = Jsoup.connect(url)
//                .userAgent(searchSettings.getAgent())
//                .referrer("http://www.google.com")
//                .execute();
//
//        String content = response.body();
//        String path = url.replaceAll(baseUrl, "");
//        int code = response.statusCode();
//        page.setCode(code);
//        page.setPath(path);
//        page.setContent(content);
//        page.setSiteId(siteId);
//        return page;
//    }
//
////    private List<Field> getFieldListFromDB() {
////        List<Field> list = new ArrayList<>();
////        Iterable<Field> iterable = fieldRepositoryService.getAllField();
////        iterable.forEach(list::add);
////        return list;
////    }
//private List<Site> getSiteListFromDB() {
//        List<Site> list = new ArrayList<>();
//        Iterable<Site> iterable = siteRepositoryService.getAllSites();
//        iterable.forEach(list::add);
//        return list;
//    }
//
//    private String getStringByTeg (String teg, String html) {
//        String string = "";
//        Document document = Jsoup.parse(html);
//        Elements elements = document.select(teg);
//        StringBuilder builder = new StringBuilder();
//        elements.forEach(element -> builder.append(element.text()).append(" "));
//        if (!builder.isEmpty()){
//            string = builder.toString();
//        }
//        return string;
//    }
//
//    private void lemmaToDB (TreeMap<String, Integer> lemmaMap, int siteId) {
//        for (Map.Entry<String, Integer> lemma : lemmaMap.entrySet()) {
//            String lemmaName = lemma.getKey();
//            List<Lemma> lemma1 = lemmaRepositoryService.getLemma(lemmaName);
//            Lemma lemma2 = lemma1.stream().
//                    filter(lemma3 -> lemma3.getSite().getId() == siteId).
//                    findFirst().
//                    orElse(null);
//            if (lemma2 == null){
//                Lemma newLemma = new Lemma(lemmaName, 1, site);
//                lemmaRepositoryService.save(newLemma);
//            } else {
//                int count = (int) lemma2.getFrequency();
//                lemma2.setFrequency(++count);
//                lemmaRepositoryService.save(lemma2);
//            }
//        }
//    }
//
//    private TreeMap<String, Float> indexingLemmas (TreeMap<String, Integer> lemmas) {
//        TreeMap<String, Float> map = new TreeMap<>();
//        for (Map.Entry<String, Integer> lemma : lemmas.entrySet()) {
//            String name = lemma.getKey();
//            float w;
//            if (!map.containsKey(name)) {
//                w = (float) lemma.getValue();
//            } else {
//                w = map.get(name) + ((float) lemma.getValue() );
//            }
//            map.put(name, w);
//        }
//        return map;
//    }
//
//    private void indexingToDb (TreeMap<String, Float> map, String path){
//        Page page = pageRepositoryService.getPage(path);
//        int pageId = page.getId();
//        int siteId = page.getSiteId();
//        for (Map.Entry<String, Float> lemma : map.entrySet()) {
//
//            String lemmaName = lemma.getKey();
//            List<Lemma> lemma1 = lemmaRepositoryService.getLemma(lemmaName);
//            for (Lemma l : lemma1) {
//                if (l.getSite().getId() == siteId) {
//                    int lemmaId = l.getId();
//                    Indexx indexx = new Indexx(pageId, lemmaId, lemma.getValue());
//                    indexRepositoryService.save(indexx);
//                }
//            }
//        }
//    }
//
//    private void prepareDbToIndexing(Page page) {
//        List<Indexx> indexxList = indexRepositoryService.getAllIndexxByPageId(page.getId());
//        List<Lemma> allLemmasIdByPage = lemmaRepositoryService.findLemmasByIndexing(indexxList);
//        lemmaRepositoryService.deleteAllLemmas(allLemmasIdByPage);
//        indexRepositoryService.deleteAllIndexx(indexxList);
//        pageRepositoryService.deletePage(page);
//    }
//}
