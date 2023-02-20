package searchengine.impl;

import org.springframework.stereotype.Service;
import searchengine.model.Indexx;
import searchengine.repository.IndexRepository;
import searchengine.services.IndexRepositoryService;

import java.util.List;

@Service
public class IndexRepoServiceImpl implements IndexRepositoryService {

    private final IndexRepository indexRepository;

    public IndexRepoServiceImpl(IndexRepository indexRepository) {
        this.indexRepository = indexRepository;
    }

    @Override
    public List<Indexx> getAllIndexingByLemmaId(int lemmaId) {
        return indexRepository.findByLemmaId(lemmaId);
    }

    @Override
    public List<Indexx> getAllIndexingByPageId(int pageId) {
        return indexRepository.findByPageId(pageId);
    }

    @Override
    public synchronized void deleteAllIndexing(List<Indexx> indexxList){
        indexRepository.deleteAll(indexxList);
    }

    @Override
    public Indexx getIndexx(int lemmaId, int pageId) {
        Indexx indexing = null;
        try{
            indexing = indexRepository.findByLemmaIdAndPageId(lemmaId, pageId);
        } catch (Exception e) {
            System.out.println("lemmaId: " + lemmaId + " + pageId: " + pageId + " not unique");
            e.printStackTrace();
        }
        return indexing;
    }
    @Override
    public synchronized void save(Indexx indexx) {
        indexRepository.save(indexx);
    }
}
