package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import searchengine.model.Lemma;
import searchengine.model.Site;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LemmaRepository extends JpaRepository<Lemma, Integer> {
    void deleteAllInBatchBySite(Site site);
    @Query(value = "select count(*) from Lemma l where l.site in :sites")
    Integer countBySites(@Param("sites") Collection<Site> siteList);
    Integer countBySite(Site site);
    List<Lemma> findAllBySite(Site site);
    @Query(value = "select frequency from Lemma l " +
            "where l.lemma = :textLemma " +
            "and l.site = :site ")
    Optional<Integer> findFrequencyByTextLemmaAndSite(
            @Param("textLemma") String textLemma,
            @Param("site") Site site);
    @Query(value = "select sum(l.frequency) from Lemma l join Site s " +
            "on s = l.site and s.type = 'INDEXED' " +
            "and l.lemma =  :textLemma " +
            "group by l.lemma", nativeQuery = true)
//    @Query(value = "select sum(l.frequency) from Lemma " +
//                   "where l.site and s.type = 'INDEXED' " +
//                   "and l.lemma =  :textLemma")
    Optional<Integer> findFrequencyByTextLemma(
            @Param("textLemma") String textLemma);

//List<Lemma> findByLemma (String lemma);
//    @Query(value = "SELECT * from search_lemma WHERE id IN(:id)", nativeQuery = true)
//    List<Lemma> findById (int[] id);
//    @Query(value = "SELECT count(*) from Lemma where site_id = :id")
//    long count(@Param("id") long id);
}