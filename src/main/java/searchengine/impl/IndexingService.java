package searchengine.impl;

import searchengine.config.SiteConfig;
import searchengine.dto.indexing.IndexingResponse;
import searchengine.model.Site;
import searchengine.model.StatusType;

import java.io.IOException;

public interface IndexingService {
    IndexingResponse getStartIndexing();

    IndexingResponse getStopIndexing();

    IndexingResponse getIndexingPage(String url);

    Site creatingSite(SiteConfig siteConfig, StatusType status);

    boolean isStopFlag();
}
