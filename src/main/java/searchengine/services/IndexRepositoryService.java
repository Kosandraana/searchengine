package searchengine.services;

import searchengine.model.Indexx;

import java.util.List;

public interface IndexRepositoryService {
    List<Indexx> getAllIndexingByLemmaId(int lemmaId);
    List<Indexx> getAllIndexingByPageId(int pageId);
    void deleteAllIndexing(List<Indexx> indexxList);
    Indexx getIndexx (int lemmaId, int pageId);
    void save(Indexx indexx);

}