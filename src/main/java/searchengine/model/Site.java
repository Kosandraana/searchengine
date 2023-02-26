package searchengine.model;

import lombok.Getter;
import lombok.Setter;
import searchengine.builder.SiteBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "site")
public class Site implements Serializable {
    public static final String INDEXING = "INDEXING";
    public static final String INDEXED = "INDEXED";
    public static final String FAILED = "FAILED";
    public static final String REMOVING = "REMOVING";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private int id;
//    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED', 'REMOVING')",
            nullable = false)
    private StatusType status;

    @Column(name = "status_time", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Column(name = "url", nullable = false, columnDefinition = "varchar(255)")
    private String url;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Page> page;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "site")
//    private List<Lemma> lemma;
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<Page> pages = new HashSet<>();
    @OneToMany(mappedBy = "site", fetch = FetchType.LAZY)
    private Set<Lemma> lemmas = new HashSet<>();

//    public Site(int id, StatusType status, long statusTime, String lastError, String url, String name) {
//        this.id = id;
//        this.status = status;
//        this.statusTime = statusTime;
//        this.lastError = lastError;
//        this.url = url;
//        this.name = name;
//    }

    @Transient
    private SiteBuilder siteBuilder;
    @Transient
    private Long lastPageReadingTime = 0L;
    public String getLastError() {
    return lastError == null ? "" : lastError;
}
    @Override
    public int hashCode() {
        return url.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Site.class;
    }
    @Override
    public String toString() {
        return "id: " + id + ", url: " + url + ", name: " + name;
    }
}