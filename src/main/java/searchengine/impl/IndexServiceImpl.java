package searchengine.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import searchengine.responses.ErrorResponse;
import searchengine.responses.Response;
import searchengine.services.Indexing;
import searchengine.services.IndexingService;

@Service
public class IndexServiceImpl implements IndexingService {
    private final Indexing indexing;

    private static final Log log = LogFactory.getLog(IndexServiceImpl.class);

    public IndexServiceImpl(Indexing indexing) {
        this.indexing = indexing;
    }

    @Override
    public Response startIndexingAll() {
        Response response;
        boolean indexing1;
        try {
            indexing1 = indexing.allSiteIndexing();
            log.info("Попытка запуска индексации всех сайтов");
        } catch (InterruptedException e) {
            response = new ErrorResponse("Ошибка запуска индексации");
            log.error("Ошибка запуска индексации", e);
            return response;
        }
        if (indexing1) {
            response = new Response();
            log.info("Индексация всех сайтов запущена");
        } else {
            response = new ErrorResponse("Индексация уже запущена");
            log.warn("Индексация всех сайтов не запущена. Т.к. процесс индексации был запущен ранее.");
        }
        return response;
    }

    @Override
    public Response stopIndexing() {
        boolean indexing1 = indexing.stopSiteIndexing();
        log.info("Попытка остановки индексации");
        Response response;
        if (indexing1) {
            response = new Response();
            log.info("Индексация остановлена");
        } else {
            response = new ErrorResponse("Индексация не запущена");
            log.warn("Остановка индексации не может быть выполнена, потому что процесс индексации не запущен.");
        }
        return response;
    }

    @Override
    public Response startIndexingOne(String url) {
        Response resp;
        String response;
        try {
            response = indexing.checkedSiteIndexing(url);
        } catch (InterruptedException e) {
            resp = new ErrorResponse("Ошибка запуска индексации");
            return resp;
        }

        if (response.equals("not found")) {
            resp = new ErrorResponse("Страница находится за пределами сайтов," +
                    " указанных в конфигурационном файле");
        }
        else if (response.equals("false")) {
            resp = new ErrorResponse("Индексация страницы уже запущена");
        }
        else {
            resp = new Response();
        }
        return resp;
    }
}

//
//    public IndexServiceImpl(String linkUrl) {
//    }
//    public IndexServiceImpl() {
//
//    }
//
//    public void startIndexing() {
//        addSites();
//    }
//
//    public void addSites() {
////        LocalDateTime localDateTime = LocalDateTime.now();
//        Site site = new Site();
//        site.setId(0);
//        site.setStatus(String.valueOf(StatusType.INDEXING));
//        site.setStatusTime(new Date().getTime());
//        site.setLastError(lastError);
//        site.setUrl(site.getUrl());
//        site.setName(site.getName());
//        siteRepository.save(site);
//        site.setStatus(String.valueOf(StatusType.INDEXED));
//        siteRepository.save(site);
//    }
//
//    public void addPages() {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        Page page = new Page();
//        page.setId(0);
//        page.setContent(page.getContent());
//        page.setCode(page.getCode());
//        page.setPath(page.getPath());
//        page.setSiteId(page.getId());
//        pageRepository.save(page);
//    }
//
//    public static boolean IsStart() {
//        return IsStart();
//    }
//
//    protected void compute() {
//        try {
//            Document document = (Document) Jsoup.connect(site.getUrl()).get();
//            Elements elements = document.select("a[href]");
//            List<ForkJoinTask<Void>> tasks = new ArrayList<>();
//            for (Element element : elements) {
//                String linkUrl = element.attr("href");
//                IndexServiceImpl task = new IndexServiceImpl(linkUrl);
////                tasks.add(task.fork());
//            }
//            for (ForkJoinTask<Void> task : tasks) {
//                task.join();
////                pageRepository.saveAll(elements);
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }
//}
