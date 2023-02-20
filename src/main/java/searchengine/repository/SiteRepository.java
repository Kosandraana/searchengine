package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import searchengine.model.Site;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {
//    Optional<Site> findByUrlAndStatus(String url, String status);
//    Optional<Site> findByNameAndStatus(String name, String status);
//    Integer countByStatus(String status);
//    List<Site> findAllByStatus(String status);
//    @Transactional
//    void deleteByStatus(String status);
//    int countAllBy();
//
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query(value = "alter table site auto_increment = 0", nativeQuery = true)
//    void resetIdOnSite();
    Site findByUrl (String url);
}
