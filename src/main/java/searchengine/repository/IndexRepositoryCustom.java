package searchengine.repository;

import searchengine.model.Indexx;

import java.util.List;

public interface IndexRepositoryCustom {
    void insertIndexList(String siteName, List<Indexx> indexes);
}
