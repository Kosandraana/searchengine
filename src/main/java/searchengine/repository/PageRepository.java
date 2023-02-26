package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Site;

import java.util.Collection;
import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    @Query(value = "select count(*) from Page p where p.site in :sites")
    Integer countBySites(@Param("sites") Collection<Site> siteList);
    Integer countBySite(Site site);
    List<Page> findAllBySiteAndPathAndCode(Site site, String path, int code);
//Page findByPath (String path);
//    Optional<Page> findByIdAndSiteId (int id, int siteId);
//    @Query(value = "SELECT count(*) from Page where site_id = :id")
//    long count(@Param("id") long id);
}
