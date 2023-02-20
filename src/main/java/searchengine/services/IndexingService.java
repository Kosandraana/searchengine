package searchengine.services;

import searchengine.responses.Response;

public interface IndexingService {
    Response startIndexingAll();
    Response stopIndexing();
    Response startIndexingOne(String url);
}
